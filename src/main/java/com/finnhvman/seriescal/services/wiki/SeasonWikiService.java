package com.finnhvman.seriescal.services.wiki;

import com.finnhvman.seriescal.model.SeasonUpdate;

import java.util.List;

public interface SeasonWikiService {
    boolean isQuerying();
    boolean queryContains(List<Long> seasonIds);
    List<SeasonUpdate> getNewEpisodes(List<Long> seasonIds);
    void queryNewEpisodes(List<Long> seasonIds);
}
