package com.finnhvman.seriescal.model;

import java.util.List;

public class SeasonUpdate {

    private Long id;
    private List<Integer> newEpisodes;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getNewEpisodes() {
        return newEpisodes;
    }

    public void setNewEpisodes(List<Integer> newEpisodes) {
        this.newEpisodes = newEpisodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
