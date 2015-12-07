package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.model.SeasonUpdate;
import com.finnhvman.seriescal.services.wiki.SeasonWikiService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainControllerTest {

    @InjectMocks
    private MainController underTest;
    @Mock
    private SeasonWikiService seasonWikiService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void ifNotQueryingEpisodes_SubmitNewEpisodesQuery_ShouldStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(false);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonWikiService).queryNewEpisodes(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingSpecifiedEpisodes_SubmitNewEpisodesQuery_ShouldNotStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(true);
        Mockito.when(seasonWikiService.queryContains(seasonIds)).thenReturn(true);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonWikiService, Mockito.never()).queryNewEpisodes(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingNotSpecifiedEpisodes_SubmitNewEpisodesQuery_ShouldStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(true);
        Mockito.when(seasonWikiService.queryContains(seasonIds)).thenReturn(false);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonWikiService).queryNewEpisodes(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void ifNotQueryingEpisodes_GetNewEpisodesQuery_ShouldReturnTheNewEpisodes() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);
        SeasonUpdate seasonUpdate1 = new SeasonUpdate();
        seasonUpdate1.setTitle("Title1");
        seasonUpdate1.setId(1L);
        seasonUpdate1.setNewEpisodes(Collections.singletonList(13));
        SeasonUpdate seasonUpdate2 = new SeasonUpdate();
        seasonUpdate2.setTitle("Title2");
        seasonUpdate2.setId(2L);
        seasonUpdate2.setNewEpisodes(Collections.singletonList(20));

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(false);
        Mockito.when(seasonWikiService.getNewEpisodes(seasonIds))
                .thenReturn(Arrays.asList(seasonUpdate1, seasonUpdate2));


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);
        List<SeasonUpdate> seasonUpdates = (List<SeasonUpdate>) responseEntity.getBody();


        Assert.assertEquals(2, seasonUpdates.size());
        Assert.assertTrue(seasonUpdates.contains(seasonUpdate1));
        Assert.assertTrue(seasonUpdates.contains(seasonUpdate2));
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingSpecifiedEpisodes_GetNewEpisodesQuery_ShouldReturnPartOfTheNewEpisodes() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);
        SeasonUpdate seasonUpdate1 = new SeasonUpdate();
        seasonUpdate1.setTitle("Title1");
        seasonUpdate1.setId(1L);
        seasonUpdate1.setNewEpisodes(Collections.singletonList(13));

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(true);
        Mockito.when(seasonWikiService.queryContains(seasonIds)).thenReturn(true);
        Mockito.when(seasonWikiService.getNewEpisodes(seasonIds))
                .thenReturn(Collections.singletonList(seasonUpdate1));


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);
        List<SeasonUpdate> seasonUpdates = (List<SeasonUpdate>) responseEntity.getBody();


        Assert.assertEquals(1, seasonUpdates.size());
        Assert.assertTrue(seasonUpdates.contains(seasonUpdate1));
        Assert.assertEquals(HttpStatus.PARTIAL_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingNotSpecifiedEpisodes_GetNewEpisodesQuery_ShouldFail() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonWikiService.isQuerying()).thenReturn(true);
        Mockito.when(seasonWikiService.queryContains(seasonIds)).thenReturn(false);


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);


        Assert.assertEquals("Part or all of the seasons specified in the request are not being or have not been queried. " +
                "A new query with the seasons specified in the request has to be submitted  beforehand.", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
    }

}
