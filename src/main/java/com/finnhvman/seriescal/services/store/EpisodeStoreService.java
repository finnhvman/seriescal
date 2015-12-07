package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Episode;

import java.util.List;
import java.util.Map;

public interface EpisodeStoreService {

    List<Episode> getAllEpisodes(Long seasonId);

    void updateEpisodes(Long seasonId, Map<Integer, Integer> episodeDates);

    List<Integer> getNewEpisodeNumbers(Long seasonId);

    void mark(Long seasonId, Integer episodeNumber, Boolean enqueued);

}