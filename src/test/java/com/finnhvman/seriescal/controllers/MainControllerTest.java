package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.model.SeasonNews;
import com.finnhvman.seriescal.services.query.SeasonUpdatesService;
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
    /*

    @InjectMocks
    private MainController underTest;
    @Mock
    private SeasonUpdatesService seasonUpdatesService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void ifNotQueryingEpisodes_SubmitNewEpisodesQuery_ShouldStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(false);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonUpdatesService).querySeasonUpdates(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingSpecifiedEpisodes_SubmitNewEpisodesQuery_ShouldNotStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(true);
        Mockito.when(seasonUpdatesService.queryContains(seasonIds)).thenReturn(true);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonUpdatesService, Mockito.never()).querySeasonUpdates(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingNotSpecifiedEpisodes_SubmitNewEpisodesQuery_ShouldStartANewQuery() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(true);
        Mockito.when(seasonUpdatesService.queryContains(seasonIds)).thenReturn(false);


        ResponseEntity responseEntity = underTest.submitNewEpisodesQuery(seasonIds);


        Mockito.verify(seasonUpdatesService).querySeasonUpdates(seasonIds);

        Assert.assertEquals("Querying...", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void ifNotQueryingEpisodes_GetNewEpisodesQuery_ShouldReturnTheNewEpisodes() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);
        SeasonNews seasonNews1 = new SeasonNews();
        seasonNews1.setTitle("Title1");
        seasonNews1.setId(1L);
        seasonNews1.setNewEpisodes(Collections.singleton(13));
        SeasonNews seasonNews2 = new SeasonNews();
        seasonNews2.setTitle("Title2");
        seasonNews2.setId(2L);
        seasonNews2.setNewEpisodes(Collections.singleton(20));

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(false);
        Mockito.when(seasonUpdatesService.getAllSeasonNews(seasonIds))
                .thenReturn(Arrays.asList(seasonNews1, seasonNews2));


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);
        List<SeasonNews> seasonNewses = (List<SeasonNews>) responseEntity.getBody();


        Assert.assertEquals(2, seasonNewses.size());
        Assert.assertTrue(seasonNewses.contains(seasonNews1));
        Assert.assertTrue(seasonNewses.contains(seasonNews2));
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingSpecifiedEpisodes_GetNewEpisodesQuery_ShouldReturnPartOfTheNewEpisodes() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);
        SeasonNews seasonNews1 = new SeasonNews();
        seasonNews1.setTitle("Title1");
        seasonNews1.setId(1L);
        seasonNews1.setNewEpisodes(Collections.singleton(13));

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(true);
        Mockito.when(seasonUpdatesService.queryContains(seasonIds)).thenReturn(true);
        Mockito.when(seasonUpdatesService.getAllSeasonNews(seasonIds))
                .thenReturn(Collections.singletonList(seasonNews1));


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);
        List<SeasonNews> seasonNewses = (List<SeasonNews>) responseEntity.getBody();


        Assert.assertEquals(1, seasonNewses.size());
        Assert.assertTrue(seasonNewses.contains(seasonNews1));
        Assert.assertEquals(HttpStatus.PARTIAL_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void ifQueryingNotSpecifiedEpisodes_GetNewEpisodesQuery_ShouldFail() throws Exception {
        List<Long> seasonIds = Arrays.asList(1L, 2L, 3L);

        Mockito.when(seasonUpdatesService.isQuerying()).thenReturn(true);
        Mockito.when(seasonUpdatesService.queryContains(seasonIds)).thenReturn(false);


        ResponseEntity responseEntity = underTest.getNewEpisodesQuery(seasonIds);


        Assert.assertEquals("Part or all of the seasons specified in the request are not being or have not been queried. " +
                "A new query with the seasons specified in the request has to be submitted  beforehand.", responseEntity.getBody());
        Assert.assertEquals(HttpStatus.PRECONDITION_FAILED, responseEntity.getStatusCode());
    }
    */

}
