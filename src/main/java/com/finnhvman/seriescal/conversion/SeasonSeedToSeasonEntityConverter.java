package com.finnhvman.seriescal.conversion;

import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.wiki.parsers.UrlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class SeasonSeedToSeasonEntityConverter implements Converter<SeasonSeed, SeasonEntity> {

    @Autowired
    private UrlParser urlParser;

    @Override
    public SeasonEntity convert(SeasonSeed source) {
        SeasonEntity seasonEntity = new SeasonEntity();
        String url = source.getUrl();
        try {
            seasonEntity.setWikiUrl(urlParser.extractWikiUrl(url));
            seasonEntity.setPage(urlParser.extractPage(url));
            seasonEntity.setTitle(urlParser.extractTitle(url));
            seasonEntity.setTouched(-1L);
            seasonEntity.setSection(-1);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return seasonEntity;
    }

}
