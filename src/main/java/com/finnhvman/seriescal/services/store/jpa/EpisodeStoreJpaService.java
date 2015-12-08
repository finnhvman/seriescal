package com.finnhvman.seriescal.services.store.jpa;

import com.finnhvman.seriescal.conversion.EpisodeEntityToEpisodeConverter;
import com.finnhvman.seriescal.model.Episode;
import com.finnhvman.seriescal.services.store.EpisodeStoreService;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntityPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EpisodeStoreJpaService implements EpisodeStoreService {

    @Autowired
    private EpisodeCrudRepository episodeCrudRepository;
    @Autowired
    private EpisodeEntityToEpisodeConverter episodeEntityToEpisodeConverter;

    @Override
    public List<Episode> getAllEpisodes(Long seasonId) {
        return episodeCrudRepository.findBySeasonId(seasonId).parallelStream()
                .map(episodeEntityToEpisodeConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void updateEpisodes(Long seasonId, Map<Integer, Integer> episodeDates) {
        for(Map.Entry<Integer, Integer> entry : episodeDates.entrySet()) {
            EpisodeEntity episodeEntity = episodeCrudRepository.findOne(new EpisodeEntityPK(seasonId, entry.getKey()));
            if (episodeEntity == null) {
                episodeEntity = new EpisodeEntity();
                episodeEntity.setSeasonId(seasonId);
                episodeEntity.setNumber(entry.getKey());
                episodeEntity.setDate(entry.getValue());
                episodeCrudRepository.save(episodeEntity);
            } else {
                if (!episodeEntity.getDate().equals(entry.getValue())) {
                    episodeEntity.setDate(entry.getValue());
                    episodeCrudRepository.save(episodeEntity);
                }
            }
        }
    }

    @Override
    public List<Integer> getNewEpisodeNumbers(Long seasonId) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int date = (year % 100) * 10000 + month * 100 + day;
        return episodeCrudRepository.findBySeasonIdAndEnqueuedAndDateLessThan(seasonId, false, date).parallelStream()
                .map(EpisodeEntity::getNumber)
                .collect(Collectors.toList());
    }

    @Override
    public void mark(Long seasonId, Integer episodeNumber, Boolean enqueued) {
        EpisodeEntityPK episodeEntityPK = new EpisodeEntityPK(seasonId, episodeNumber);
        EpisodeEntity episodeEntity = episodeCrudRepository.findOne(episodeEntityPK);
        episodeEntity.setEnqueued(enqueued);
        episodeCrudRepository.save(episodeEntity);
    }

}
