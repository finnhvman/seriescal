package com.finnhvman.seriescal.services.store.jpa;

import com.finnhvman.seriescal.conversion.EpisodeEntityToEpisodeConverter;
import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntityPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EpisodeStoreJpaService implements EpisodeStoreService {

    @Autowired
    private EpisodeCrudRepository episodeCrudRepository;
    @Autowired
    private EpisodeEntityToEpisodeConverter episodeEntityToEpisodeConverter;

    @Override
    public Set<Episode> getAllEpisodes(Long seasonId) {
        return episodeCrudRepository.findBySeasonId(seasonId).parallelStream()
                .map(episodeEntityToEpisodeConverter::convert)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateEpisodes(Long seasonId, Map<Integer, Integer> episodeDates) {
        for(Map.Entry<Integer, Integer> entry : episodeDates.entrySet()) {
            EpisodeEntity episodeEntity = episodeCrudRepository.findOne(new EpisodeEntityPK(seasonId, entry.getKey()));
            if (episodeEntity == null) {
                addEpisodeEntity(seasonId, entry);
            } else {
                updateEpisodeEntityIfChanged(episodeEntity, entry.getValue());
            }
        }
    }

    private void addEpisodeEntity(Long seasonId, Map.Entry<Integer, Integer> entry) {
        EpisodeEntity episodeEntity = new EpisodeEntity();
        episodeEntity.setSeasonId(seasonId);
        episodeEntity.setNumber(entry.getKey());
        episodeEntity.setDate(entry.getValue());
        episodeEntity.setEnqueued(false);
        episodeCrudRepository.save(episodeEntity);
    }

    private void updateEpisodeEntityIfChanged(EpisodeEntity episodeEntity, Integer date) {
        if (!episodeEntity.getDate().equals(date)) {
            episodeEntity.setDate(date);
            episodeCrudRepository.save(episodeEntity);
        }
    }

    @Override
    public Set<Integer> getNewEpisodeNumbers(Long seasonId) {
        int date = getCurrentDate();
        return episodeCrudRepository.findBySeasonIdAndEnqueuedAndDateLessThan(seasonId, false, date).parallelStream()
                .map(EpisodeEntity::getNumber)
                .collect(Collectors.toSet());
    }

    private Integer getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return (year % 100) * 10000 + month * 100 + day;
    }

    @Override
    public void mark(Long seasonId, Integer episodeNumber, Boolean enqueued) {
        EpisodeEntityPK episodeEntityPK = new EpisodeEntityPK(seasonId, episodeNumber);
        EpisodeEntity episodeEntity = episodeCrudRepository.findOne(episodeEntityPK);
        episodeEntity.setEnqueued(enqueued);
        episodeCrudRepository.save(episodeEntity);
    }

}
