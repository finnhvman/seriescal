package com.finnhvman.seriescal.services.store;

import com.finnhvman.seriescal.SeriesCalApplication;
import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.jpa.SeasonCrudRepository;
import com.finnhvman.seriescal.services.store.jpa.SeasonStoreJpaService;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeriesCalApplication.class)
public class SeasonStoreJpaServiceIntegrationTest {

    // TODO more test, negative tests

    @Autowired
    private SeasonStoreJpaService underTest;
    @Autowired
    private SeasonCrudRepository seasonCrudRepository;

    private static final String TBBT9 = "https://en.wikipedia.org/wiki/The_Big_Bang_Theory_%28season_9%29";
    private static final String BCS2 = "https://en.wikipedia.org/wiki/List_of_Better_Call_Saul_episodes#Season_2_.282016.29";
    private static final String MRR1 = "https://en.wikipedia.org/wiki/Mr._Robot_%28TV_series%29#Season_1_.282015.29";

    @Before
    public void cleanUp() {
        seasonCrudRepository.deleteAll();
    }

    @Test
    public void testGetSeasonEntity() throws Exception {
        SeasonEntity seasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());


        SeasonEntity result = underTest.getSeasonEntity(seasonEntity.getId());


        Assert.assertEquals(seasonEntity.getWikiUrl(), result.getWikiUrl());
        Assert.assertEquals(seasonEntity.getPage(), result.getPage());
        Assert.assertEquals(seasonEntity.getTitle(), result.getTitle());
        Assert.assertEquals(seasonEntity.getSection(), result.getSection());
        Assert.assertEquals(seasonEntity.getTouched(), result.getTouched());

    }

    @Test
    public void testAdd() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(BCS2);


        Season season = underTest.add(seasonSeed);


        SeasonEntity result = seasonCrudRepository.findOne(season.getId());

        Assert.assertEquals("List_of_Better_Call_Saul_episodes#Season_2_.282016.29", result.getWikiUrl());
        Assert.assertEquals("List_of_Better_Call_Saul_episodes", result.getPage());
        Assert.assertEquals("Better Call Saul (Season 2)", result.getTitle());
        Assert.assertEquals(Integer.valueOf(-1), result.getSection());
        Assert.assertEquals(Long.valueOf(-1L), result.getTouched());
    }

    @Test
    public void testUpdateSeasonSeed() throws Exception {
        SeasonEntity seasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());

        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(TBBT9);


        underTest.update(seasonEntity.getId(), seasonSeed);


        SeasonEntity result = seasonCrudRepository.findOne(seasonEntity.getId());

        Assert.assertEquals("The_Big_Bang_Theory_%28season_9%29", result.getWikiUrl());
        Assert.assertEquals("The_Big_Bang_Theory_(season_9)", result.getPage());
        Assert.assertEquals("The Big Bang Theory (Season 9)", result.getTitle());
    }

    @Test
    public void testGetAllSeason() throws Exception {
        SeasonEntity bcs2SeasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());
        SeasonEntity tbbt9SeasonEntity = seasonCrudRepository.save(createTbbt9SeasonEntity());


        List<Season> results = underTest.getAllSeason();


        Assert.assertEquals(2, results.size());
        List<Long> resultIds = results.stream().map(Season::getId).collect(Collectors.toList());
        Assert.assertTrue(resultIds.contains(bcs2SeasonEntity.getId()));
        Assert.assertTrue(resultIds.contains(tbbt9SeasonEntity.getId()));
    }

    @Test
    public void testRemove() throws Exception {
        SeasonEntity seasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());


        underTest.remove(seasonEntity.getId());


        SeasonEntity result = seasonCrudRepository.findOne(seasonEntity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testGetSeasonsPages() throws Exception {
        SeasonEntity bcs2SeasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());
        SeasonEntity tbbt9SeasonEntity = seasonCrudRepository.save(createTbbt9SeasonEntity());


        Set<String> result = underTest.getSeasonsPages(Arrays.asList(bcs2SeasonEntity.getId(), tbbt9SeasonEntity.getId()));


        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains("List_of_Better_Call_Saul_episodes"));
        Assert.assertTrue(result.contains("The_Big_Bang_Theory_(season_9)"));
    }

    @Test
    public void testUpdateTouchedOfTouchedSeasonsFoundByPage() throws Exception {
        seasonCrudRepository.save(createBcs1SeasonEntity());
        seasonCrudRepository.save(createBcs2SeasonEntity());
        seasonCrudRepository.save(createTbbt9SeasonEntity());


        List<Long> resultIds = underTest.updateTouchedOfTouchedSeasonsFoundByPage("List_of_Better_Call_Saul_episodes", 1L);


        SeasonEntity result1 = seasonCrudRepository.findOne(resultIds.get(0));
        SeasonEntity result2 = seasonCrudRepository.findOne(resultIds.get(1));

        Assert.assertEquals(2, resultIds.size());
        Assert.assertEquals(Long.valueOf(1L), result1.getTouched());
        Assert.assertEquals(Long.valueOf(1L), result2.getTouched());
    }

    @Test
    public void testUpdateSectionOfSeason() throws Exception {
        SeasonEntity seasonEntity = seasonCrudRepository.save(createBcs2SeasonEntity());


        underTest.updateSectionOfSeason(seasonEntity.getId(), 1);


        SeasonEntity result = seasonCrudRepository.findOne(seasonEntity.getId());
        Assert.assertEquals(Integer.valueOf(1), result.getSection());
    }

    private SeasonEntity createBcs1SeasonEntity() {
        SeasonEntity seasonEntity = new SeasonEntity();
        seasonEntity.setWikiUrl("List_of_Better_Call_Saul_episodes#Season_1_.282015.29");
        seasonEntity.setPage("List_of_Better_Call_Saul_episodes");
        seasonEntity.setTitle("Better Call Saul (Season 1)");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        return seasonEntity;
    }

    private SeasonEntity createBcs2SeasonEntity() {
        SeasonEntity seasonEntity = new SeasonEntity();
        seasonEntity.setWikiUrl("List_of_Better_Call_Saul_episodes#Season_2_.282016.29");
        seasonEntity.setPage("List_of_Better_Call_Saul_episodes");
        seasonEntity.setTitle("Better Call Saul (Season 2)");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        return seasonEntity;
    }

    private SeasonEntity createTbbt9SeasonEntity() {
        SeasonEntity seasonEntity = new SeasonEntity();
        seasonEntity.setWikiUrl("The_Big_Bang_Theory_%28season_9%29");
        seasonEntity.setPage("The_Big_Bang_Theory_(season_9)");
        seasonEntity.setTitle("The Big Bang Theory (Season 9)");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        return seasonEntity;
    }

}
