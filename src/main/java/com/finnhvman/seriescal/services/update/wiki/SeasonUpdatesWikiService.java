package com.finnhvman.seriescal.services.update.wiki;

import com.finnhvman.seriescal.model.SeasonNews;
import com.finnhvman.seriescal.services.update.SeasonUpdatesService;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.SeasonStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.services.wiki.SeasonWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeasonUpdatesWikiService implements SeasonUpdatesService {

    @Autowired
    private SeasonWikiService seasonWikiService;
    @Autowired
    private SeasonStoreService seasonStoreService;
    @Autowired
    private EpisodeStoreService episodeStoreService;

    private Thread collectorThread;
    private volatile int collectorProgress = 100;

    @Override
    public boolean isQuerying() {
        return collectorProgress != 100;
    }

    @Override
    public int getQueryProgress() {
        return collectorProgress;
    }

    @Override
    public void querySeasonUpdates() {
        try {
            collectorProgress = 0;
            List<Long> seasonIds = seasonStoreService.getAllSeasonIds();
            List<Long> touchedSeasonIds = getAndUpdateTouchedSeasons(seasonIds);
            startCollectorThread(touchedSeasonIds);
        } catch (InterruptedException e) {
            // TODO should not do anything
        } catch (ParseException e) {
            // TODO somehow notify about the fault
        } finally {
            collectorProgress = 100;
        }
    }

    private List<Long> getAndUpdateTouchedSeasons(List<Long> seasonIds) throws ParseException {
        Set<String> pages = seasonStoreService.getSeasonsPages(seasonIds);
        Map<String, Long> touchedTimes = seasonWikiService.getTouchedTimes(pages);
        return touchedTimes.entrySet().stream()
                .flatMap(entry -> seasonStoreService.updateTouchedOfTouchedSeasonsFoundByPage(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());
    }

    private void startCollectorThread(List<Long> touchedSeasonIds) throws InterruptedException {
        if (collectorThread != null && collectorThread.isAlive()) {
            collectorThread.interrupt();
            collectorThread.join();
        }
        collectorThread = new Thread(() -> collectSeasonUpdates(touchedSeasonIds));
        collectorThread.start();
    }

    private void collectSeasonUpdates(List<Long> touchedSeasonIds) {
        for (int index = 0; index < touchedSeasonIds.size(); index++) {
            try {
                if (Thread.interrupted()) {
                    break;
                }
                collectSeasonUpdate(touchedSeasonIds.get(index));
            } catch (ParseException e) {
                e.printStackTrace(); // TODO somehow note the fault, also catch unchecked exceptions?
            } finally {
                collectorProgress = index / touchedSeasonIds.size() * 100;
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

    @Override
    public List<SeasonNews> getAllSeasonNews() {
        return seasonStoreService.getAllSeasonIds().parallelStream()
                .map(this::getSeasonNews)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SeasonNews getSeasonNews(Long seasonId) {
        Set<Integer> newEpisodeNumbers = episodeStoreService.getNewEpisodeNumbers(seasonId);
        if (!newEpisodeNumbers.isEmpty()) {
            SeasonEntity seasonEntity = seasonStoreService.getSeasonEntity(seasonId);
            SeasonNews seasonNews = new SeasonNews();
            seasonNews.setId(seasonEntity.getId());
            seasonNews.setTitle(seasonEntity.getTitle());
            seasonNews.setNewEpisodes(newEpisodeNumbers);
            return seasonNews;
        }
        return null;
    }

}
