package com.finnhvman.seriescal.services.wiki.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.finnhvman.seriescal.model.SeasonUpdate;
import com.finnhvman.seriescal.repository.SeasonCrudRepository;
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
    private SeasonCrudRepository seasonCrudRepository;
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
            List<SeasonEntity> touchedSeasons = getTouchedSeasons(seasonIds);
            startCollectorThread(() -> collectSeasonUpdates(touchedSeasons));
        } catch (InterruptedException e) {
            // TODO should not do anything
        } catch (ParseException e) {
            // TODO revisit this
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

    private List<SeasonEntity> getTouchedSeasons(List<Long> seasonIds) throws ParseException {
        List<String> pages = getSeasonPages(seasonIds);
        String pipedPages = pipePages(pages);
        JsonNode seasonsInfo = wikiRestTemplate.queryInfo(pipedPages);
        return filterTouchedSeasons(seasonsInfo);
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

    private List<SeasonEntity> filterTouchedSeasons(JsonNode touchedInfo) throws ParseException {
        Map<String, Long> touchedTimes = parserFacade.extractTouchedTimes(touchedInfo);

        List<SeasonEntity> touchedSeasonEntities = new ArrayList<>(); // TODO maybe there is a lambda solution
        for (Map.Entry<String, Long> entry : touchedTimes.entrySet()) {
            List<SeasonEntity> seasonEntities = seasonCrudRepository.findByPage(entry.getKey());
            seasonEntities.parallelStream().
                    filter(seasonEntity -> seasonEntity.getTouched() < entry.getValue())
                    .forEach(seasonEntity -> {
                        seasonEntity.setTouched(entry.getValue());
                        touchedSeasonEntities.add(seasonEntity);
                    });
        }
        return touchedSeasonEntities;
    }

    private void startCollectorThread(Runnable runnable) throws InterruptedException {
        if (collectorThread != null) {
            collectorThread.interrupt();
            collectorThread.join();
        }
        collectorThread = new Thread(runnable);
        collectorThread.start();
    }

    private void collectSeasonUpdates(List<SeasonEntity> seasonEntities) {
        for (SeasonEntity seasonEntity : seasonEntities) {
            if (Thread.interrupted()) {
                break;
            }
            SeasonUpdate seasonUpdate = collectSeasonUpdate(seasonEntity);
            if (seasonUpdate != null) {
                seasonUpdatesCache.add(seasonUpdate);
            }
        }
    }

    private SeasonUpdate collectSeasonUpdate(SeasonEntity seasonEntity) {
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
        try {
            return getSeasonUpdate(seasonEntity);
        } catch (ParseException e) {
            return null;
        }
    }

    private int getSectionIndex(SeasonEntity seasonEntity) {
        JsonNode sections = wikiRestTemplate.parseSections(seasonEntity.getPage());
        String seasonNumber = null;
        try {
            seasonNumber = parserFacade.extractSeasonNumber(seasonEntity.getWikiUrl());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int sectionNumber = parserFacade.extractSectionNumber(sections, seasonNumber);
        seasonEntity.setSection(sectionNumber);
        return sectionNumber;
    }

    private SeasonUpdate getSeasonUpdate(SeasonEntity seasonEntity) throws ParseException {
        JsonNode wikiSection = wikiRestTemplate.parseSection(seasonEntity.getPage(), seasonEntity.getSection());
        Map<Integer, Date> episodeDates = parserFacade.extractEpisodeDates(wikiSection);
        // convert map to seasonUpdate TODO
        return null;
    }

}
