package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Episode;

import java.util.List;

public interface EpisodeStoreService {

    List<Episode> getAllEpisodes(Long seasonId);

    void mark(Long seasonId, Integer episodeNumber, Boolean enqueued);

}