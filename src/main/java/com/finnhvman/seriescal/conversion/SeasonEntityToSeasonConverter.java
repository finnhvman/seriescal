package com.finnhvman.seriescal.conversion;

import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.model.Season;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SeasonEntityToSeasonConverter implements Converter<SeasonEntity, Season> {

    @Override
    public Season convert(SeasonEntity seasonEntity) {
        Season season = new Season();
        season.setTitle(seasonEntity.getTitle());
        season.setId(seasonEntity.getId());
        return season;
    }

}
