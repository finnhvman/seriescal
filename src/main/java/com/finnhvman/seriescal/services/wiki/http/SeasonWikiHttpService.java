package com.finnhvman.seriescal.services.wiki.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.finnhvman.seriescal.services.wiki.ParserFacade;
import com.finnhvman.seriescal.services.wiki.SeasonWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

@Component
public class SeasonWikiHttpService implements SeasonWikiService {

    private static final String PIPE = "|";

    @Autowired
    private ParserFacade parserFacade;
    @Autowired
    private WikiRestTemplate wikiRestTemplate;

    @Override
    public Map<String, Long> getTouchedTimes(Collection<String> pages) throws ParseException {
        String pipedPages = pipePages(pages);
        JsonNode pagesInfo = wikiRestTemplate.queryInfo(pipedPages);
        return parserFacade.extractTouchedTimes(pagesInfo);
    }

    private String pipePages(Collection<String> pages) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String page : pages) {
            stringBuilder.append(page);
            stringBuilder.append(PIPE);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    @Override
    public int getSectionIndex(String wikiUrl) throws ParseException {
        String page = parserFacade.extractPage(wikiUrl);
        JsonNode sections = wikiRestTemplate.parseSections(page);
        String seasonNumber = parserFacade.extractSeasonNumber(wikiUrl);
        return parserFacade.extractSectionIndex(sections, seasonNumber);
    }

    @Override
    public Map<Integer, Integer> getEpisodeDates(String page, int section) throws ParseException {
        JsonNode wikiSection = wikiRestTemplate.parseSection(page, section);
        return parserFacade.extractEpisodeDates(wikiSection);
    }

}
