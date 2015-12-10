package com.finnhvman.seriescal.services.store.jpa;

import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.model.Season;
import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.SeasonStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
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
    public SeasonEntity getSeasonEntity(Long seasonId) {
        return seasonCrudRepository.findOne(seasonId);
    }

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

    @Override
    public Set<String> getSeasonsPages(List<Long> seasonIds) {
        List<SeasonEntity> seasonEntities = seasonCrudRepository.findByIdIn(seasonIds);
        return seasonEntities.parallelStream()
                .map(SeasonEntity::getPage)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Long> updateTouchedOfTouchedSeasonsFoundByPage(String page, Long touched) {
        return seasonCrudRepository.findByPage(page).stream()
                .filter(seasonEntity -> seasonEntity.getTouched() < touched)
                .map(seasonEntity -> {
                    seasonEntity.setTouched(touched);
                    seasonCrudRepository.save(seasonEntity);
                    return seasonEntity.getId();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateSectionOfSeason(Long seasonId, Integer section) {
        SeasonEntity seasonEntity = seasonCrudRepository.findOne(seasonId);
        seasonEntity.setSection(section);
        seasonCrudRepository.save(seasonEntity);
    }

}
