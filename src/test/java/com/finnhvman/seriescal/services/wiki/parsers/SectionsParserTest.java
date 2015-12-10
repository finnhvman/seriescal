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
    public void testExtractSectionIndexOnEmptySections() throws Exception {
        JsonNode sections = readJson("EmptySections.json");

        underTest.extractSectionIndex(sections, "0");
    }

    @Test
    public void testExtractSectionIndexOnBobsBurgersSeason6() throws Exception {
        JsonNode sections = readJson("BobsBurgersSeason6Sections.json");

        int sectionIndex = underTest.extractSectionIndex(sections, "6");

        Assert.assertEquals(1, sectionIndex);
    }

    @Test
    public void testExtractSectionIndexOnFearTheWalkingDead() throws Exception {
        JsonNode sections = readJson("FearTheWalkingDeadSections.json");

        int sectionIndex = underTest.extractSectionIndex(sections, "1");

        Assert.assertEquals(4, sectionIndex);
    }

    @Test
    public void testExtractSectionIndexOnMrRobot() throws Exception {
        JsonNode sections = readJson("MrRobotSections.json");

        int sectionIndex = underTest.extractSectionIndex(sections, "1");

        Assert.assertEquals(6, sectionIndex);
    }

    @Test
    public void testExtractSectionIndexOnSherlock() throws Exception {
        JsonNode sections = readJson("SherlockSections.json");

        int sectionIndex = underTest.extractSectionIndex(sections, "3");

        Assert.assertEquals(5, sectionIndex);
    }

    @Test
    public void testExtractSectionIndexOnTheBigBangTheorySeason8() throws Exception {
        JsonNode sections = readJson("TheBigBangTheorySeason8Sections.json");

        int sectionIndex = underTest.extractSectionIndex(sections, "8");

        Assert.assertEquals(5, sectionIndex);
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\sections\\" + fileName));
    }

}
