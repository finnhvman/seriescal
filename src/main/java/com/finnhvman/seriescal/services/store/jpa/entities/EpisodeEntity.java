package com.finnhvman.seriescal.services.store.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EpisodeEntity {

    @Id
    private Integer number;
    private Boolean enqueued;
    private Long seasonId;


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getEnqueued() {
        return enqueued;
    }

    public void setEnqueued(Boolean enqueued) {
        this.enqueued = enqueued;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }
}
