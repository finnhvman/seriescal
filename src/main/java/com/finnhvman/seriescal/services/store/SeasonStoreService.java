package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;

import java.util.List;

public interface SeasonStoreService {
    List<Season> getAllSeason();
    Long add(SeasonSeed seasonSeed);
    void update(Long seasonId, SeasonSeed seasonSeed);
    void remove(Long seasonId);
}
