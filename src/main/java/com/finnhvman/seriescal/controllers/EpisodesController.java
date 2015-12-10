package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/seasons")
public class EpisodesController {

    @Autowired
    private EpisodeStoreService episodeStoreService;

    @RequestMapping(value = "/{seasonId}", method = RequestMethod.GET)
    public Collection<Episode> listEpisodes(@PathVariable Long seasonId) {
        return episodeStoreService.getAllEpisodes(seasonId);
    }

    @RequestMapping(value = "/{seasonId}/{episodeNumber}", method = RequestMethod.PUT)
    public void markEpisode(@PathVariable Long seasonId, @PathVariable Integer episodeNumber, @RequestParam Boolean enqueued) {
        episodeStoreService.mark(seasonId, episodeNumber, enqueued);
    }

}
