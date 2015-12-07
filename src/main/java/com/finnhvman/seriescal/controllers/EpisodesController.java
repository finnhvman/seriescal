package com.finnhvman.seriescal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.model.Episode;

import java.util.List;

@RestController
@RequestMapping("/seasons")
public class EpisodesController {

    @Autowired
    private EpisodeStoreService episodeStoreService;

    @RequestMapping(value = "/{seasonId}", method = RequestMethod.GET)
    public List<Episode> listEpisodes(@PathVariable Long seasonId) {
        return episodeStoreService.getAllEpisodes(seasonId);
    }

    @RequestMapping(value = "/{seasonId}/{episodeNumber}", method = RequestMethod.PUT)
    public void markEpisode(@PathVariable Long seasonId, @PathVariable Integer episodeNumber, @RequestParam Boolean enqueued) {
        episodeStoreService.mark(seasonId, episodeNumber, enqueued);
    }

}
