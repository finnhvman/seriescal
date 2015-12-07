package com.finnhvman.seriescal.services.wiki;

import com.fasterxml.jackson.databind.JsonNode;
import com.finnhvman.seriescal.services.wiki.parsers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Component
public class ParserFacade {

    // TODO HANDLE ALL ERRORS, maybe use ParseException everywhere

    @Autowired
    private InfoParser infoParser;
    @Autowired
    private SectionsParser sectionsParser;
    @Autowired
    private WikiTextParser wikiTextParser;
    @Autowired
    private UrlParser urlParser;

    public Map<String, Long> extractTouchedTimes(JsonNode info)  {
        return infoParser.extractTouchedTimes(info);
    }

    public Integer extractSectionNumber(JsonNode info, String seasonNumber) {
        return sectionsParser.extractSectionNumber(info, seasonNumber);
    }

    public Map<Integer, Date> extractEpisodeDates(JsonNode wikiSection) throws ParseException {
        return wikiTextParser.extractEpisodeDates(wikiSection);
    }

    public String extractWikiUrl(String url) throws ParseException {
        return urlParser.extractWikiUrl(url);
    }

    public String extractPage(String url) throws ParseException {
        return urlParser.extractPage(url);
    }

    public String extractSeriesTitle(String url) throws ParseException {
        return urlParser.extractSeriesTitle(url);
    }

    public String extractSeasonNumber(String url) throws ParseException {
        return urlParser.extractSeasonNumber(url);
    }

    public String extractTitle(String url) throws ParseException {
        return urlParser.extractTitle(url);
    }

}
