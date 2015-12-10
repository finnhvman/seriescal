package com.finnhvman.seriescal.controllers;

import com.finnhvman.seriescal.services.query.NewEpisodesQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    // TODO validation

    @Autowired
    private NewEpisodesQueryService newEpisodesQueryService;

    @RequestMapping(value = "/new-episodes-query", method = RequestMethod.PUT)
    public ResponseEntity<?> submitNewEpisodesQuery(@RequestParam List<Long> seasonIds) {
        if (newEpisodesQueryService.isQuerying()) {
            if (!newEpisodesQueryService.queryContains(seasonIds)) {
                newEpisodesQueryService.queryNewEpisodes(seasonIds);
            }
        } else {
            newEpisodesQueryService.queryNewEpisodes(seasonIds);
        }
        return new ResponseEntity<>("Querying...", HttpStatus.OK);
    }

    @RequestMapping(value = "/new-episodes-query", method = RequestMethod.GET)
    public ResponseEntity<?> getNewEpisodesQuery(@RequestParam List<Long> seasonIds) {
        if (newEpisodesQueryService.isQuerying()) {
            if (newEpisodesQueryService.queryContains(seasonIds)) {
                // TODO UI gets partial data
                return new ResponseEntity<>(newEpisodesQueryService.getNewEpisodes(seasonIds), HttpStatus.PARTIAL_CONTENT);
            } else {
                return new ResponseEntity<>("Part or all of the seasons specified in the request are not being or have not been queried. " +
                        "A new query with the seasons specified in the request has to be submitted  beforehand.", HttpStatus.PRECONDITION_FAILED);
            }
        } else {
            return new ResponseEntity<>(newEpisodesQueryService.getNewEpisodes(seasonIds), HttpStatus.OK);
        }
    }

}
