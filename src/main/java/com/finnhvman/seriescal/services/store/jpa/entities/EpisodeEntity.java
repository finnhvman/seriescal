package com.finnhvman.seriescal.services.store.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(EpisodeEntityPK.class)
public class EpisodeEntity {

    @Id
    private Long seasonId;
    @Id
    private Integer number;
    private Integer date;
    private Boolean enqueued;

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Boolean getEnqueued() {
        return enqueued;
    }

    public void setEnqueued(Boolean enqueued) {
        this.enqueued = enqueued;
    }

}
