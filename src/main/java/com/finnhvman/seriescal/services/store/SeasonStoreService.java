package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;

import java.util.List;
import java.util.Set;

public interface SeasonStoreService {
    SeasonEntity getSeasonEntity(Long seasonId);

    List<Season> getAllSeason();
    Season add(SeasonSeed seasonSeed);
    Season update(Long seasonId, SeasonSeed seasonSeed);
    void remove(Long seasonId);

    Set<String> getSeasonsPages(List<Long> seasonIds);
    List<Long> updateTouchedOfTouchedSeasonsFoundByPage(String page, Long touched);
    void updateSectionOfSeason(Long seasonId, Integer section);
}
