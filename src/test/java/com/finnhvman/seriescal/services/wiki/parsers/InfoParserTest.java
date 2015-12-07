package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class InfoParserTest {

    private InfoParser underTest = new InfoParser();

    @Test
    public void testExtractSectionNumberOnBobsBurgersSeason6() throws Exception {
        JsonNode info = readJson("5PageBatchInfo.json");

        Map<String, Long> touchedTimes = underTest.extractTouchedTimes(info);

        Assert.assertEquals(5, touchedTimes.size());
        Assert.assertEquals(Long.valueOf(1449022201000L), touchedTimes.get("List_of_Sherlock_episodes"));
        Assert.assertEquals(Long.valueOf(1448749712000L), touchedTimes.get("The_Big_Bang_Theory_(season_8)"));
        Assert.assertEquals(Long.valueOf(1449083380000L), touchedTimes.get("Mr._Robot_(TV series)"));
        Assert.assertEquals(Long.valueOf(1448243397000L), touchedTimes.get("Bob's_Burgers_(season_6)"));
        Assert.assertEquals(Long.valueOf(1448971243000L), touchedTimes.get("Fear_the_Walking_Dead"));
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\info\\" + fileName));
    }

}
