package com.finnhvman.seriescal.services.wiki.parsers;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class UrlParserTest {

    private static final String SEASON_URL = "https://en.wikipedia.org/wiki/The_Big_Bang_Theory_%28season_9%29";
    private static final String EPISODES_URL = "https://en.wikipedia.org/wiki/List_of_Better_Call_Saul_episodes#Season_2_.282016.29";
    private static final String SERIES_URL = "https://en.wikipedia.org/wiki/Mr._Robot_%28TV_series%29#Season_1_.282015.29";

    private UrlParser underTest = new UrlParser();

    @Test(expected = ParseException.class)
    public void testExtractWikiUrlOfEmptyString() throws Exception {
        underTest.extractWikiUrl("");
    }

    @Test(expected = ParseException.class)
    public void testExtractPageOfEmptyString() throws Exception {
        underTest.extractPage("");
    }

    @Test(expected = ParseException.class)
    public void testExtractTitleOfEmptyString() throws Exception {
        underTest.extractTitle("");
    }

    @Test
    public void testExtractWikiUrlOfSeasonUrl() throws Exception {
        String seasonWikiUrl = underTest.extractWikiUrl(SEASON_URL);

        Assert.assertEquals("The_Big_Bang_Theory_%28season_9%29", seasonWikiUrl);
    }

    @Test
    public void testExtractPageOfSeasonUrl() throws Exception {
        String seasonPage = underTest.extractPage(SEASON_URL);

        Assert.assertEquals("The_Big_Bang_Theory_(season_9)", seasonPage);
    }

    @Test
    public void testExtractTitleOfSeasonUrl() throws Exception {
        String seasonTitle = underTest.extractTitle(SEASON_URL);

        Assert.assertEquals("The Big Bang Theory (Season 9)", seasonTitle);
    }

    @Test
    public void testExtractWikiUrlOfEpisodesUrl() throws Exception {
        String episodesWikiUrl = underTest.extractWikiUrl(EPISODES_URL);

        Assert.assertEquals("List_of_Better_Call_Saul_episodes#Season_2_.282016.29", episodesWikiUrl);
    }

    @Test
    public void testExtractPageOfEpisodesUrl() throws Exception {
        String episodesPage = underTest.extractPage(EPISODES_URL);

        Assert.assertEquals("List_of_Better_Call_Saul_episodes", episodesPage);
    }

    @Test
    public void testExtractTitleOfEpisodesUrl() throws Exception {
        String episodesTitle = underTest.extractTitle(EPISODES_URL);

        Assert.assertEquals("Better Call Saul (Season 2)", episodesTitle);
    }

    @Test
    public void testExtractWikiUrlOfSeriesUrl() throws Exception {
        String seriesWikiUrl = underTest.extractWikiUrl(SERIES_URL);

        Assert.assertEquals("Mr._Robot_%28TV_series%29#Season_1_.282015.29", seriesWikiUrl);
    }

    @Test
    public void testExtractPageOfSeriesUrl() throws Exception {
        String seriesPage = underTest.extractPage(SERIES_URL);

        Assert.assertEquals("Mr._Robot_(TV_series)", seriesPage);
    }

    @Test
    public void testExtractTitleOfSeriesUrl() throws Exception {
        String seriesTitle = underTest.extractTitle(SERIES_URL);

        Assert.assertEquals("Mr. Robot (Season 1)", seriesTitle);
    }

    @Test
    public void testExtractTitleOfSeriesUrlWithoutSeason() throws Exception {
        String seriesTitle = underTest.extractTitle("https://en.wikipedia.org/wiki/Fear_the_Walking_Dead");

        Assert.assertEquals("Fear the Walking Dead (Season 1)", seriesTitle);
    }

}
