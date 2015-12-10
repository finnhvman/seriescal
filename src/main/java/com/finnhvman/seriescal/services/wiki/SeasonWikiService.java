package com.finnhvman.seriescal.services.wiki;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

public interface SeasonWikiService {
    Map<String, Long> getTouchedTimes(Collection<String> pages) throws ParseException;
    int getSectionIndex(String wikiUrl) throws ParseException;
    Map<Integer, Integer> getEpisodeDates(String page, int section) throws ParseException;
}
