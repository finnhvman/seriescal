package com.finnhvman.seriescal.services.query.wiki;

import com.finnhvman.seriescal.model.SeasonUpdate;
import com.finnhvman.seriescal.services.query.NewEpisodesQueryService;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.SeasonStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.services.wiki.SeasonWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class NewEpisodesQueryWikiService implements NewEpisodesQueryService {

    @Autowired
    private SeasonWikiService seasonWikiService;
    @Autowired
    private SeasonStoreService seasonStoreService;
    @Autowired
    private EpisodeStoreService episodeStoreService;

    private List<Long> seasonsBeingQueried = Collections.synchronizedList(new ArrayList<>());
    private List<SeasonUpdate> seasonUpdatesCache = Collections.synchronizedList(new ArrayList<>());
    private Thread collectAndCacheThread;

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
            seasonsBeingQueried.addAll(seasonIds);
            invalidateCache(seasonIds);
            seasonIds.forEach(this::cacheSeasonUpdate);
            List<Long> touchedSeasonIds = getAndUpdateTouchedSeasons(seasonIds);
            startCollectAndCacheThread(touchedSeasonIds);
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

    private void invalidateCache(Long seasonId) {
        invalidateCache(Collections.singletonList(seasonId));
    }

    private void cacheSeasonUpdate(Long seasonId) {
        Set<Integer> newEpisodeNumbers = episodeStoreService.getNewEpisodeNumbers(seasonId);
        if (!newEpisodeNumbers.isEmpty()) {
            SeasonEntity seasonEntity = seasonStoreService.getSeasonEntity(seasonId);
            SeasonUpdate seasonUpdate = new SeasonUpdate();
            seasonUpdate.setId(seasonEntity.getId());
            seasonUpdate.setTitle(seasonEntity.getTitle());
            seasonUpdate.setNewEpisodes(newEpisodeNumbers);
            seasonUpdatesCache.add(seasonUpdate);
        }
    }

    private List<Long> getAndUpdateTouchedSeasons(List<Long> seasonIds) throws ParseException {
        Set<String> pages = seasonStoreService.getSeasonsPages(seasonIds);
        Map<String, Long> touchedTimes = seasonWikiService.getTouchedTimes(pages);
        return touchedTimes.entrySet().stream()
                .flatMap(entry -> seasonStoreService.updateTouchedOfTouchedSeasonsFoundByPage(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());
    }

    private void startCollectAndCacheThread(List<Long> touchedSeasonIds) throws InterruptedException {
        if (collectAndCacheThread != null && collectAndCacheThread.isAlive()) {
            collectAndCacheThread.interrupt();
            collectAndCacheThread.join();
        }
        collectAndCacheThread = new Thread(() -> collectAndCacheSeasonUpdates(touchedSeasonIds));
        collectAndCacheThread.start();
    }

    private void collectAndCacheSeasonUpdates(List<Long> touchedSeasonIds) {
        for (Long touchedSeasonId : touchedSeasonIds) {
            try {
                if (Thread.interrupted()) {
                    break;
                }
                collectSeasonUpdate(touchedSeasonId);
                invalidateCache(touchedSeasonId);
                cacheSeasonUpdate(touchedSeasonId);
            } catch (ParseException e) {
                e.printStackTrace(); // TODO somehow note the fault, also catch unchecked exceptions?
            }
        }
    }

    private void collectSeasonUpdate(Long seasonId) throws ParseException {
        try {
            updateEpisodeDates(seasonId);
        } catch (ParseException e) {
            updateSectionIndex(seasonId);
            updateEpisodeDates(seasonId);
        }
    }

    private void updateEpisodeDates(Long seasonId) throws ParseException {
        SeasonEntity seasonEntity = seasonStoreService.getSeasonEntity(seasonId);
        if (seasonEntity.getSection() < 0) {
            throw new ParseException("Invalid section index.", 0);
        } else {
            Map<Integer, Integer> episodeDates = seasonWikiService.getEpisodeDates(seasonEntity.getPage(), seasonEntity.getSection());
            episodeStoreService.updateEpisodes(seasonEntity.getId(), episodeDates);
        }
    }

    private void updateSectionIndex(Long seasonId) throws ParseException {
        SeasonEntity seasonEntity = seasonStoreService.getSeasonEntity(seasonId);
        Integer sectionIndex = seasonWikiService.getSectionIndex(seasonEntity.getWikiUrl());
        seasonStoreService.updateSectionOfSeason(seasonId, sectionIndex);
    }

}
