package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.SeasonStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class SeasonsControllerTest {

    @InjectMocks
    private SeasonsController underTest;
    @Mock
    private SeasonStoreService seasonStoreService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListSeasons() {
        Season season = new Season();
        season.setId(1L);
        season.setTitle("Season");

        Mockito.when(seasonStoreService.getAllSeason()).thenReturn(Collections.singletonList(season));


        List<Season> seasons = underTest.listSeasons();


        Assert.assertEquals(1, seasons.size());
        Assert.assertEquals(season, seasons.get(0));
    }

    @Test
    public void testAddSeason() {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl("url");

        Mockito.when(seasonStoreService.add(seasonSeed)).thenReturn(1L);


        ResponseEntity responseEntity = underTest.addSeason(seasonSeed);


        Assert.assertEquals(1L, responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateSeason() {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl("url");

        ResponseEntity responseEntity = underTest.updateSeason(1L, seasonSeed);

        Mockito.verify(seasonStoreService).update(1L, seasonSeed);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRemoveSeason() {
        ResponseEntity responseEntity = underTest.removeSeason(1L);

        Mockito.verify(seasonStoreService).remove(1L);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
