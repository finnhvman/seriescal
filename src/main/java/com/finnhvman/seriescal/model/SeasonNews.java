package com.finnhvman.seriescal.model;

import java.util.Set;

public class SeasonNews {

    private Long id;
    private Set<Integer> newEpisodes;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Integer> getNewEpisodes() {
        return newEpisodes;
    }

    public void setNewEpisodes(Set<Integer> newEpisodes) {
        this.newEpisodes = newEpisodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
