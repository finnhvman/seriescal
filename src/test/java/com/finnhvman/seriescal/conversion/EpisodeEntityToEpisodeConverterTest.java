package com.finnhvman.seriescal.conversion;

import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import org.junit.Assert;
import org.junit.Test;

public class EpisodeEntityToEpisodeConverterTest {

    private EpisodeEntityToEpisodeConverter underTest = new EpisodeEntityToEpisodeConverter();

    @Test
    public void testConvert() {
        EpisodeEntity episodeEntity = new EpisodeEntity();
        episodeEntity.setNumber(1);
        episodeEntity.setEnqueued(true);

        Episode episode = underTest.convert(episodeEntity);

        Assert.assertEquals(episodeEntity.getNumber(), episode.getNumber());
        Assert.assertEquals(episodeEntity.getEnqueued(), episode.getEnqueued());
    }

}
