package com.finnhvman.seriescal.services.wiki;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finnhvman.seriescal.services.wiki.parsers.InfoParser;
import com.finnhvman.seriescal.services.wiki.parsers.SectionsParser;
import com.finnhvman.seriescal.services.wiki.parsers.UrlParser;
import com.finnhvman.seriescal.services.wiki.parsers.WikiTextParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ParserFacadeTest {

    @InjectMocks
    private ParserFacade underTest;
    @Mock
    private InfoParser infoParser;
    @Mock
    private SectionsParser sectionsParser;
    @Mock
    private WikiTextParser wikiTextParser;
    @Mock
    private UrlParser urlParser;

    private JsonNode mockJson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockJson = new ObjectMapper().readTree("{}");
    }

    @Test
    public void testExtractTouchedTimes() {
        underTest.extractTouchedTimes(mockJson);

        Mockito.verify(infoParser).extractTouchedTimes(mockJson);
    }

    @Test
    public void testExtractSectionNumber() {
        underTest.extractSectionNumber(mockJson, "1");

        Mockito.verify(sectionsParser).extractSectionNumber(mockJson, "1");
    }

    @Test
    public void testExtractEpisodeDates() throws Exception {
        underTest.extractEpisodeDates(mockJson);

        Mockito.verify(wikiTextParser).extractEpisodeDates(mockJson);
    }

    @Test
    public void testExtractWikiUrl() throws Exception {
        underTest.extractWikiUrl("url");

        Mockito.verify(urlParser).extractWikiUrl("url");
    }

    @Test
    public void testExtractPage() throws Exception {
        underTest.extractPage("url");

        Mockito.verify(urlParser).extractPage("url");
    }

    @Test
    public void testExtractSeriesTitle() throws Exception {
        underTest.extractSeriesTitle("url");

        Mockito.verify(urlParser).extractSeriesTitle("url");
    }

    @Test
    public void testExtractSeasonNumber() throws Exception {
        underTest.extractSeasonNumber("url");

        Mockito.verify(urlParser).extractSeasonNumber("url");
    }

    @Test
    public void testExtractTitle() throws Exception {
        underTest.extractTitle("url");

        Mockito.verify(urlParser).extractTitle("url");
    }

}
