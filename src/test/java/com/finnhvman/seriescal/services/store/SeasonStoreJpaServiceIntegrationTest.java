package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.SeriesCalApplication;
import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.jpa.SeasonCrudRepository;
import com.finnhvman.seriescal.services.store.jpa.SeasonStoreJpaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeriesCalApplication.class)
public class SeasonStoreJpaServiceIntegrationTest {

    // TODO more test, negative tests

    @Autowired
    private SeasonStoreJpaService underTest;
    @Autowired
    private SeasonCrudRepository seasonCrudRepository;

    private static final String SEASON_URL = "https://en.wikipedia.org/wiki/The_Big_Bang_Theory_%28season_9%29";
    private static final String EPISODES_URL = "https://en.wikipedia.org/wiki/List_of_Better_Call_Saul_episodes#Season_2_.282016.29";
    private static final String SERIES_URL = "https://en.wikipedia.org/wiki/Mr._Robot_%28TV_series%29#Season_1_.282015.29";

    @Before
    public void cleanUp() {
        seasonCrudRepository.deleteAll();
    }

    @Test
    public void testAdd() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(SEASON_URL);


        Long id = underTest.add(seasonSeed);


        Assert.assertNotNull(id);
    }

    @Test
    public void testUpdate() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(SEASON_URL);

        SeasonSeed updatedSeasonSeed = new SeasonSeed();
        updatedSeasonSeed.setUrl(EPISODES_URL);

        Long id = underTest.add(seasonSeed);


        underTest.update(id, updatedSeasonSeed);


        List<Season> seasons = underTest.getAllSeason();
        Season season = seasons.get(0);

        Assert.assertEquals(1, seasons.size());
        Assert.assertEquals("Better Call Saul (Season 2)", season.getTitle());


        Assert.assertNotNull(id);
    }

    @Test
    public void testGetAllSeason() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(SEASON_URL);

        underTest.add(seasonSeed);


        List<Season> seasons = underTest.getAllSeason();


        Season season = seasons.get(0);

        Assert.assertEquals(1, seasons.size());
        Assert.assertEquals("The Big Bang Theory (Season 9)", season.getTitle());
    }

    @Test
    public void testRemove() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(SEASON_URL);

        Long id = underTest.add(seasonSeed);


        underTest.remove(id);


        List<Season> seasons = underTest.getAllSeason();

        Assert.assertEquals(0, seasons.size());
    }



}
