package com.finnhvman.seriescal.services.store.jpa;

import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.SeasonStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeasonStoreJpaService implements SeasonStoreService {

    @Autowired
    private SeasonCrudRepository seasonCrudRepository;
    @Autowired
    private Converter<SeasonEntity, Season> seasonEntityToSeasonConverter;
    @Autowired
    private Converter<SeasonSeed, SeasonEntity> seasonSeedToSeasonEntityConverter;

    @Override
    public List<Season> getAllSeason() {
        return seasonCrudRepository.findAll().parallelStream()
                .map(seasonEntityToSeasonConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Long add(SeasonSeed seasonSeed) {
        SeasonEntity seasonEntity = seasonSeedToSeasonEntityConverter.convert(seasonSeed);
        seasonEntity = seasonCrudRepository.save(seasonEntity);
        return seasonEntity.getId();
    }

    @Override
    public void update(Long seasonId, SeasonSeed seasonSeed) {
        SeasonEntity seasonEntity = seasonSeedToSeasonEntityConverter.convert(seasonSeed);
        seasonEntity.setId(seasonId);
        seasonCrudRepository.save(seasonEntity);
    }

    @Override
    public void remove(Long seasonId) {
        seasonCrudRepository.delete(seasonId);
    }

}
