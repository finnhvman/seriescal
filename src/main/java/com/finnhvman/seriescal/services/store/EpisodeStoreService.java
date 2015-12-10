package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Episode;

import java.util.Map;
import java.util.Set;

public interface EpisodeStoreService {

    Set<Episode> getAllEpisodes(Long seasonId);

    void updateEpisodes(Long seasonId, Map<Integer, Integer> episodeDates);

    Set<Integer> getNewEpisodeNumbers(Long seasonId);

    void mark(Long seasonId, Integer episodeNumber, Boolean enqueued);

}