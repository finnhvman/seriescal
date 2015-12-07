package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class SectionsParserTest {

    private SectionsParser underTest = new SectionsParser();

    @Test(expected = ParseException.class)
    public void testExtractSectionNumberOnEmptySections() throws Exception {
        JsonNode sections = readJson("EmptySections.json");

        underTest.extractSectionNumber(sections, "0");
    }

    @Test
    public void testExtractSectionNumberOnBobsBurgersSeason6() throws Exception {
        JsonNode sections = readJson("BobsBurgersSeason6Sections.json");

        int sectionNumber = underTest.extractSectionNumber(sections, "6");

        Assert.assertEquals(1, sectionNumber);
    }

    @Test
    public void testExtractSectionNumberOnFearTheWalkingDead() throws Exception {
        JsonNode sections = readJson("FearTheWalkingDeadSections.json");

        int sectionNumber = underTest.extractSectionNumber(sections, "1");

        Assert.assertEquals(4, sectionNumber);
    }

    @Test
    public void testExtractSectionNumberOnMrRobot() throws Exception {
        JsonNode sections = readJson("MrRobotSections.json");

        int sectionNumber = underTest.extractSectionNumber(sections, "1");

        Assert.assertEquals(6, sectionNumber);
    }

    @Test
    public void testExtractSectionNumberOnSherlock() throws Exception {
        JsonNode sections = readJson("SherlockSections.json");

        int sectionNumber = underTest.extractSectionNumber(sections, "3");

        Assert.assertEquals(5, sectionNumber);
    }

    @Test
    public void testExtractSectionNumberOnTheBigBangTheorySeason8() throws Exception {
        JsonNode sections = readJson("TheBigBangTheorySeason8Sections.json");

        int sectionNumber = underTest.extractSectionNumber(sections, "8");

        Assert.assertEquals(5, sectionNumber);
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\sections\\" + fileName));
    }

}
