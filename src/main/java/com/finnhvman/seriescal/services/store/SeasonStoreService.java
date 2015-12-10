package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;

import java.util.List;
import java.util.Set;

public interface SeasonStoreService {
    SeasonEntity getSeasonEntity(Long seasonId);
    void update(SeasonEntity seasonEntity);

    List<Season> getAllSeason();
    Long add(SeasonSeed seasonSeed);
    void update(Long seasonId, SeasonSeed seasonSeed);
    void remove(Long seasonId);

    Set<String> getSeasonPages(List<Long> seasonIds);
    List<Long> updateTouchedSeasons(String page, Long touchedTime);
}
