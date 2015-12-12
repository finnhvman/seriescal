package com.finnhvman.seriescal.services.wiki;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finnhvman.seriescal.SeriesCalApplication;
import com.finnhvman.seriescal.services.wiki.http.SeasonWikiHttpService;
import com.finnhvman.seriescal.services.wiki.http.WikiRestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeriesCalApplication.class)
public class SeasonWikiHttpServiceIntegrationTest {

    private static final String WIKIPEDIA_API = "https://en.wikipedia.org/w/api.php";
    private static final String MRR1 = "Mr._Robot_%28TV_series%29#Season_1_.282015.29";

    @Autowired
    private SeasonWikiHttpService underTest;
    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        RestTemplate restTemplate = getRestTemplateThroughReflection();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetTouchedTimes() throws Exception {
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=query&titles=Bob's_Burgers_(season_6)%7CFear_the_Walking_Dead%7CMr._Robot_(TV_series)%7CList_of_Sherlock_episodes%7CThe_Big_Bang_Theory_(season_8)&prop=info"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("info\\5PageBatchInfo.json").toString(), MediaType.APPLICATION_JSON));

        Collection<String> pages = Arrays.asList("Bob's_Burgers_(season_6)","Fear_the_Walking_Dead", "Mr._Robot_(TV_series)", "List_of_Sherlock_episodes", "The_Big_Bang_Theory_(season_8)");


        Map<String, Long> result = underTest.getTouchedTimes(pages);


        Assert.assertEquals(5, result.size());
        Assert.assertEquals(Long.valueOf(1449022201000L), result.get("List_of_Sherlock_episodes"));
        Assert.assertEquals(Long.valueOf(1448749712000L), result.get("The_Big_Bang_Theory_(season_8)"));
        Assert.assertEquals(Long.valueOf(1449083380000L), result.get("Mr._Robot_(TV_series)"));
        Assert.assertEquals(Long.valueOf(1448243397000L), result.get("Bob's_Burgers_(season_6)"));
        Assert.assertEquals(Long.valueOf(1448971243000L), result.get("Fear_the_Walking_Dead"));
    }

    @Test
    public void testGetSectionIndex() throws Exception {
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Mr._Robot_(TV_series)&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\MrRobotSections.json").toString(), MediaType.APPLICATION_JSON));


        int sectionIndex = underTest.getSectionIndex(MRR1);


        Assert.assertEquals(6, sectionIndex);
    }

    @Test
    public void testGetEpisodeDates() throws Exception {
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Mr._Robot_(TV_series)&section=6&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\MrRobot.Season1Section.json").toString(), MediaType.APPLICATION_JSON));


        Map<Integer, Integer> episodeDates = underTest.getEpisodeDates("Mr._Robot_(TV_series)", 6);


        Assert.assertEquals(10, episodeDates.size());
        Assert.assertEquals(Integer.valueOf(150624), episodeDates.get(1));
        Assert.assertEquals(Integer.valueOf(150902), episodeDates.get(10));
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\" + fileName));
    }

    private RestTemplate getRestTemplateThroughReflection() throws Exception {
        Field wikiRestTemplateField =  underTest.getClass().getDeclaredField("wikiRestTemplate");
        wikiRestTemplateField.setAccessible(true);
        WikiRestTemplate wikiRestTemplate = (WikiRestTemplate) wikiRestTemplateField.get(underTest);
        Field restTemplateField = wikiRestTemplate.getClass().getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        return (RestTemplate) restTemplateField.get(wikiRestTemplate);
    }

}
