package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;

public class EpisodesControllerTest {

    @InjectMocks
    private EpisodesController underTest;
    @Mock
    private EpisodeStoreService episodeStoreService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListEpisodes() {
        Episode episode = new Episode();
        episode.setNumber(1);
        episode.setEnqueued(true);

        Mockito.when(episodeStoreService.getAllEpisodes(1L)).thenReturn(Collections.singleton(episode));


        Collection<Episode> episodes = underTest.listEpisodes(1L);


        Assert.assertEquals(1, episodes.size());
        Assert.assertTrue(episodes.contains(episode));
    }

    @Test
    public void markEpisode() {
        underTest.markEpisode(1L, 1, true);

        Mockito.verify(episodeStoreService).mark(1L, 1, true);
    }
}
