package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class WikiTextParserTest {

    private WikiTextParser underTest = new WikiTextParser();

    @Test
    public void testExtractEpisodeDatesOnBobsBurgers6Episodes() throws Exception {
        JsonNode wikiSection = readJson("BobsBurgersSeason6.EpisodesSection.json");

        Map<Integer, Integer> episodeDates = underTest.extractEpisodeDates(wikiSection);

        Assert.assertEquals(5, episodeDates.size());
        assertDatesEqual(2015, 9, 27, episodeDates.get(1));
        assertDatesEqual(2015, 10, 11, episodeDates.get(2));
        assertDatesEqual(2015, 10, 18, episodeDates.get(3));
        assertDatesEqual(2015, 11, 8, episodeDates.get(4));
        assertDatesEqual(2015, 11, 15, episodeDates.get(5));
    }

    @Test
    public void testExtractEpisodeDatesOnFearTheWalkingDeadEpisodes() throws Exception {
        JsonNode wikiSection = readJson("FearTheWalkingDead.EpisodesSection.json");

        Map<Integer, Integer> episodeDates = underTest.extractEpisodeDates(wikiSection);

        Assert.assertEquals(6, episodeDates.size());
        assertDatesEqual(2015, 8, 23, episodeDates.get(1));
        assertDatesEqual(2015, 8, 30, episodeDates.get(2));
        assertDatesEqual(2015, 9, 13, episodeDates.get(3));
        assertDatesEqual(2015, 9, 20, episodeDates.get(4));
        assertDatesEqual(2015, 9, 27, episodeDates.get(5));
        assertDatesEqual(2015, 10, 4, episodeDates.get(6));
    }

    @Test
    public void testExtractEpisodeDatesOnMrRobotSeason1() throws Exception {
        JsonNode wikiSection = readJson("MrRobot.Season1Section.json");

        Map<Integer, Integer> episodeDates = underTest.extractEpisodeDates(wikiSection);

        Assert.assertEquals(10, episodeDates.size());
        assertDatesEqual(2015, 6, 24, episodeDates.get(1));
        assertDatesEqual(2015, 9, 2, episodeDates.get(10));
    }

    @Test
    public void testExtractEpisodeDatesOnSherlockSeries3() throws Exception {
        JsonNode wikiSection = readJson("Sherlock.Series3Section.json");

        Map<Integer, Integer> episodeDates = underTest.extractEpisodeDates(wikiSection);

        Assert.assertEquals(4, episodeDates.size());
        assertDatesEqual(2013, 12, 24, episodeDates.get(0));
        assertDatesEqual(2014, 1, 1, episodeDates.get(1));
        assertDatesEqual(2014, 1, 5, episodeDates.get(2));
        assertDatesEqual(2014, 1, 12, episodeDates.get(3));
    }

    @Test
    public void testExtractEpisodeDatesOnTheBigBangTheorySeason8Episodes() throws Exception {
        JsonNode wikiSection = readJson("TheBigBangTheorySeason8.EpisodesSection.json");

        Map<Integer, Integer> episodeDates = underTest.extractEpisodeDates(wikiSection);

        Assert.assertEquals(24, episodeDates.size());
        assertDatesEqual(2014, 9, 22, episodeDates.get(1));
        assertDatesEqual(2015, 5, 7, episodeDates.get(24));
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\section\\" + fileName));
    }

    private void assertDatesEqual(int year, int month, int day, int actual) {
        int expected = (year % 100) * 10000 + month * 100 + day;
        Assert.assertEquals(expected, actual);
    }

}
