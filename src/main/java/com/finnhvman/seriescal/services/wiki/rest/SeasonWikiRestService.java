package com.finnhvman.seriescal.services.wiki.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.finnhvman.seriescal.model.SeasonUpdate;
import com.finnhvman.seriescal.services.store.jpa.SeasonCrudRepository;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.services.wiki.ParserFacade;
import com.finnhvman.seriescal.services.wiki.SeasonWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SeasonWikiRestService implements SeasonWikiService {

    private static final String PIPE = "|";

    @Autowired
    private ParserFacade parserFacade;
    @Autowired
    private SeasonCrudRepository seasonCrudRepository; // TODO replace with SeasonStoreService, preferably do not use SeasonEntity
    @Autowired
    private EpisodeStoreService episodeStoreService;
    @Autowired
    private WikiRestTemplate wikiRestTemplate;

    private List<SeasonUpdate> seasonUpdatesCache = Collections.synchronizedList(new ArrayList<>());
    private List<Long> seasonsBeingQueried = new ArrayList<>();
    private Thread collectorThread;

    @Override
    public boolean isQuerying() {
        return !seasonsBeingQueried.isEmpty();
    }

    @Override
    public boolean queryContains(List<Long> seasonIds) {
        return seasonsBeingQueried.containsAll(seasonIds);
    }

    @Override
    public List<SeasonUpdate> getNewEpisodes(List<Long> seasonIds) {
        return seasonUpdatesCache.parallelStream()
                .filter(seasonUpdate -> seasonIds.contains(seasonUpdate.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void queryNewEpisodes(List<Long> seasonIds) { // TODO what if accessed via multiple threads?
        try {
            seasonsBeingQueried = new ArrayList<>(seasonIds);
            invalidateCache(seasonIds);
            List<Long> touchedSeasonIds = getTouchedSeasonIds(seasonIds);
            startCollectorThread(() -> collectSeasonUpdates(seasonIds, touchedSeasonIds));
        } catch (InterruptedException e) {
            // TODO should not do anything
        } catch (ParseException e) {
            // TODO somehow notify about the fault
        } finally {
            seasonsBeingQueried.clear();
        }
    }

    private void invalidateCache(List<Long> seasonIds) {
        Iterator<SeasonUpdate> iterator = seasonUpdatesCache.iterator();
        while (iterator.hasNext()) {
            SeasonUpdate seasonUpdate = iterator.next();
            if (seasonIds.contains(seasonUpdate.getId())) {
                iterator.remove();
            }
        }
    }

    private List<Long> getTouchedSeasonIds(List<Long> seasonIds) throws ParseException {
        List<String> pages = getSeasonPages(seasonIds);
        String pipedPages = pipePages(pages);
        JsonNode seasonsInfo = wikiRestTemplate.queryInfo(pipedPages);
        return filterTouchedSeasonIds(seasonsInfo);
    }

    private List<String> getSeasonPages(List<Long> seasonIds) {
        List<SeasonEntity> seasonEntities = seasonCrudRepository.findByIdIn(seasonIds);
        return seasonEntities.parallelStream()
                .map(SeasonEntity::getPage)
                .collect(Collectors.toList());
    }

    private String pipePages(List<String> pages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String page : pages) {
            stringBuilder.append(page);
            stringBuilder.append(PIPE);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private List<Long> filterTouchedSeasonIds(JsonNode touchedInfo) throws ParseException {
        Map<String, Long> touchedTimes = parserFacade.extractTouchedTimes(touchedInfo);

        List<Long> touchedSeasonIds = new ArrayList<>(); // TODO maybe there is a lambda solution
        for (Map.Entry<String, Long> entry : touchedTimes.entrySet()) {
            List<SeasonEntity> seasonEntities = seasonCrudRepository.findByPage(entry.getKey()); // TODO this should be optimized
            seasonEntities.stream().
                    filter(seasonEntity -> seasonEntity.getTouched() < entry.getValue())
                    .forEach(seasonEntity -> {
                        seasonEntity.setTouched(entry.getValue());
                        seasonCrudRepository.save(seasonEntity);
                        touchedSeasonIds.add(seasonEntity.getId());
                    });
        }
        return touchedSeasonIds;
    }

    private void startCollectorThread(Runnable runnable) throws InterruptedException {
        if (collectorThread != null) {
            collectorThread.interrupt();
            collectorThread.join();
        }
        collectorThread = new Thread(runnable);
        collectorThread.start();
    }

    private void collectSeasonUpdates(List<Long> seasonIds, List<Long> touchedSeasonIds) {
        seasonIds.removeAll(touchedSeasonIds);
        for (Long seasonId : seasonIds) {
            SeasonUpdate seasonUpdate = new SeasonUpdate();
            seasonUpdate.setId(seasonId);
            SeasonEntity seasonEntity = seasonCrudRepository.findOne(seasonId);
            seasonUpdate.setTitle(seasonEntity.getTitle());
            seasonUpdate.setNewEpisodes(episodeStoreService.getNewEpisodeNumbers(seasonId));
            if (!seasonUpdate.getNewEpisodes().isEmpty()) {
                seasonUpdatesCache.add(seasonUpdate);
            }
        }

        for (Long touchedSeasonId : touchedSeasonIds) {
            if (Thread.interrupted()) {
                break;
            }
            SeasonUpdate seasonUpdate = null;
            try {
                seasonUpdate = collectSeasonUpdate(touchedSeasonId);
            } catch (ParseException e) {
                e.printStackTrace(); // TODO somehow note the fault, also catch unchecked exceptions?
            }
            if (seasonUpdate != null && !seasonUpdate.getNewEpisodes().isEmpty()) {
                seasonUpdatesCache.add(seasonUpdate);
            }
        }
    }

    private SeasonUpdate collectSeasonUpdate(Long seasonId) throws ParseException {
        SeasonEntity seasonEntity = seasonCrudRepository.findOne(seasonId);
        try {
            if (seasonEntity.getSection() < 0) {
                seasonEntity.setSection(getSectionIndex(seasonEntity));
            }
            return getSeasonUpdate(seasonEntity);
        } catch (ParseException e) {
            seasonEntity.setSection(getSectionIndex(seasonEntity));
        } finally {
            seasonCrudRepository.save(seasonEntity);
        }
        return getSeasonUpdate(seasonEntity);
    }

    private int getSectionIndex(SeasonEntity seasonEntity) throws ParseException {
        JsonNode sections = wikiRestTemplate.parseSections(seasonEntity.getPage());
        String seasonNumber = parserFacade.extractSeasonNumber(seasonEntity.getWikiUrl());
        int sectionNumber = parserFacade.extractSectionNumber(sections, seasonNumber);
        seasonEntity.setSection(sectionNumber);
        return sectionNumber;
    }

    private SeasonUpdate getSeasonUpdate(SeasonEntity seasonEntity) throws ParseException {
        JsonNode wikiSection = wikiRestTemplate.parseSection(seasonEntity.getPage(), seasonEntity.getSection());
        Map<Integer, Integer> episodeDates = parserFacade.extractEpisodeDates(wikiSection);
        episodeStoreService.updateEpisodes(seasonEntity.getId(), episodeDates);

        SeasonUpdate seasonUpdate = new SeasonUpdate();
        seasonUpdate.setId(seasonEntity.getId());
        seasonUpdate.setTitle(seasonEntity.getTitle());
        seasonUpdate.setNewEpisodes(episodeStoreService.getNewEpisodeNumbers(seasonEntity.getId()));
        return seasonUpdate;
    }

}
