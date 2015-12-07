package com.finnhvman.seriescal.repository;

import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeCrudRepository extends CrudRepository<EpisodeEntity, Integer> {

    List<EpisodeEntity> findBySeasonId(Long seasonId);
}
