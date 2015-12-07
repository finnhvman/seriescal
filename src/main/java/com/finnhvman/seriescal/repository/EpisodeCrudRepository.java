package com.finnhvman.seriescal.repository;

import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntityPK;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeCrudRepository extends CrudRepository<EpisodeEntity, EpisodeEntityPK> {

    List<EpisodeEntity> findBySeasonId(Long seasonId);

    List<EpisodeEntity> findBySeasonIdAndEnqueuedAndDateLessThan(Long seasonId, Boolean enqueued, Integer date);

}
