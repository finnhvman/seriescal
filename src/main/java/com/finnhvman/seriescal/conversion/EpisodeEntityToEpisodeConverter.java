package com.finnhvman.seriescal.conversion;


import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EpisodeEntityToEpisodeConverter implements Converter<EpisodeEntity, Episode> {

    @Override
    public Episode convert(EpisodeEntity episodeEntity) {
        Episode episode = new Episode();
        episode.setNumber(episodeEntity.getNumber());
        episode.setEnqueued(episodeEntity.getEnqueued());
        return episode;
    }
}
