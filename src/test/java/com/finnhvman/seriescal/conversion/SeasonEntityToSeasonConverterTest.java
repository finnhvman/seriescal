package com.finnhvman.seriescal.conversion;

import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import org.junit.Assert;
import org.junit.Test;

public class SeasonEntityToSeasonConverterTest {

    private SeasonEntityToSeasonConverter underTest = new SeasonEntityToSeasonConverter();

    @Test
    public void testConvert() {
        SeasonEntity seasonEntity = new SeasonEntity();
        seasonEntity.setId(1L);
        seasonEntity.setTitle("Title");

        Season season = underTest.convert(seasonEntity);

        Assert.assertEquals(seasonEntity.getId(), season.getId());
        Assert.assertEquals(seasonEntity.getTitle(), season.getTitle());
    }

}
