package com.finnhvman.seriescal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finnhvman.seriescal.model.SeasonNews;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.wiki.http.WikiRestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeriesCalApplication.class)
@IntegrationTest({"server.port:9999"})
@WebAppConfiguration
public class SeriesCalApplicationTests {

    private static final String URI = "http://localhost:9999/";
    private static final String WIKIPEDIA_API = "https://en.wikipedia.org/w/api.php";

    @Autowired
    private ApplicationContext applicationContext;

	private MockRestServiceServer mockServer;
    private RestTemplate restTemplate = new TestRestTemplate();

	@Before
	public void setUp() throws Exception {
		RestTemplate restTemplate = getRestTemplateThroughReflection();
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

    @Test
    public void testQuery() throws Exception {
        mockWiki();

        parallelPostSeasons();


        startQuery();
        int progress = getQuery();
        while(progress < 100) {
            progress = getQuery();
        }


        List<SeasonNews> seasonNews = listSeasonNews();
        Assert.assertEquals(5, listSeasonNews().size());
    }

    private RestTemplate getRestTemplateThroughReflection() throws Exception {
        WikiRestTemplate wikiRestTemplate = (WikiRestTemplate) applicationContext.getBean("wikiRestTemplate");
        Field restTemplateField = wikiRestTemplate.getClass().getDeclaredField("restTemplate");
        restTemplateField.setAccessible(true);
        return (RestTemplate) restTemplateField.get(wikiRestTemplate);
    }

    private JsonNode readJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File("src\\test\\resources\\wiki\\json\\" + fileName));
    }

    private void mockWiki() throws Exception {
        RequestMatcher infoMatcher = request -> {
            if (!request.getURI().toString().startsWith(WIKIPEDIA_API + "?format=json&action=query&titles=")) {
                throw new AssertionError();
            }
            if (!request.getURI().toString().endsWith("&prop=info")) {
                throw new AssertionError();
            }
        };

        mockServer.expect(infoMatcher)
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("info\\5PageBatchInfo.json").toString(), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Mr._Robot_(TV_series)&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\MrRobotSections.json").toString(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Mr._Robot_(TV_series)&section=6&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\MrRobot.Season1Section.json").toString(), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=List_of_Sherlock_episodes&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\SherlockSections.json").toString(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=List_of_Sherlock_episodes&section=3&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\Sherlock.Series3Section.json").toString(), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=The_Big_Bang_Theory_(season_8)&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\TheBigBangTheorySeason8Sections.json").toString(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=The_Big_Bang_Theory_(season_8)&section=5&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\TheBigBangTheorySeason8.EpisodesSection.json").toString(), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Bob's_Burgers_(season_6)&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\BobsBurgersSeason6Sections.json").toString(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Bob's_Burgers_(season_6)&section=1&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\BobsBurgersSeason6.EpisodesSection.json").toString(), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Fear_the_Walking_Dead&prop=sections"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("sections\\FearTheWalkingDeadSections.json").toString(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(WIKIPEDIA_API + "?format=json&action=parse&page=Fear_the_Walking_Dead&section=4&prop=wikitext"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(readJson("section\\FearTheWalkingDead.EpisodesSection.json").toString(), MediaType.APPLICATION_JSON));
    }

    private void parallelPostSeasons() {
        Arrays.asList(
                "https://en.wikipedia.org/wiki/Bob's_Burgers_%28season_6%29",
                "https://en.wikipedia.org/wiki/Fear_the_Walking_Dead",
                "https://en.wikipedia.org/wiki/Mr._Robot_%28TV_series%29",
                "https://en.wikipedia.org/wiki/List_of_Sherlock_episodes#Series_3_.282013.E2.80.9314.29",
                "https://en.wikipedia.org/wiki/The_Big_Bang_Theory_%28season_8%29"
        ).parallelStream()
                .forEach(url -> {
                    SeasonSeed seasonSeed = new SeasonSeed();
                    seasonSeed.setUrl(url);
                    restTemplate.postForLocation(URI + "seasons", seasonSeed);
                });
    }

    private void startQuery() {
        restTemplate.postForLocation(URI + "seasons/updates-query", null);
    }

    private int getQuery() {
        ResponseEntity responseEntity = restTemplate.getForEntity(URI + "seasons/updates-query", Integer.class);
        return (int) responseEntity.getBody();
    }

    private List<SeasonNews> listSeasonNews() {
        ResponseEntity responseEntity = restTemplate.getForEntity(URI + "seasons/new-episodes", List.class);
        return (List<SeasonNews>) responseEntity.getBody();
    }

}
