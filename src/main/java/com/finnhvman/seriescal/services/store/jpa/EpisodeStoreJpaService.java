package com.finnhvman.seriescal.services.store.jpa;

import com.finnhvman.seriescal.conversion.EpisodeEntityToEpisodeConverter;
import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.repository.EpisodeCrudRepository;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EpisodeStoreJpaService implements EpisodeStoreService {

    @Autowired
    private EpisodeCrudRepository episodeCrudRepository;
    @Autowired
    private EpisodeEntityToEpisodeConverter episodeEntityToEpisodeConverter;

    @Override
    public List<Episode> getAllEpisodes(Long seasonId) {
        return episodeCrudRepository.findBySeasonId(seasonId).parallelStream()
                .map(episodeEntityToEpisodeConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void mark(Long seasonId, Integer episodeNumber, Boolean enqueued) {
        EpisodeEntity episodeEntity = episodeCrudRepository.findOne(episodeNumber);
        episodeEntity.setEnqueued(enqueued);
        episodeCrudRepository.save(episodeEntity);
    }
}
