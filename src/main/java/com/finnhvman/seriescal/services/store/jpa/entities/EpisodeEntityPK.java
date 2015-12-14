package com.finnhvman.seriescal.services.store.jpa.entities;

import java.io.Serializable;

public class EpisodeEntityPK implements Serializable {

    private Long seasonId;
    private Integer number;

    public EpisodeEntityPK() {
    }

    public EpisodeEntityPK(Long seasonId, Integer number) {
        this.seasonId = seasonId;
        this.number = number;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public Integer getNumber() {
        return number;
    }

}
