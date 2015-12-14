package com.finnhvman.seriescal.services.update;

import com.finnhvman.seriescal.SeriesCalApplication;
import com.finnhvman.seriescal.model.SeasonNews;
import com.finnhvman.seriescal.services.store.jpa.EpisodeCrudRepository;
import com.finnhvman.seriescal.services.store.jpa.SeasonCrudRepository;
import com.finnhvman.seriescal.services.store.jpa.entities.EpisodeEntity;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.services.update.wiki.SeasonUpdatesWikiService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeriesCalApplication.class)
public class SeasonUpdatesWikiServiceIntegrationTest {

    @Autowired
    private SeasonUpdatesWikiService underTest;
    @Autowired
    private SeasonCrudRepository seasonCrudRepository;
    @Autowired
    private EpisodeCrudRepository episodeCrudRepository;

    @Test
    public void testGetAllSeasonNews() throws Exception {
        SeasonEntity seasonEntity = new SeasonEntity();
        seasonEntity.setTitle("Season 1");
        seasonEntity.setWikiUrl("url");
        seasonEntity.setPage("page");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        seasonEntity = seasonCrudRepository.save(seasonEntity);
        final long id1 = seasonEntity.getId();

        EpisodeEntity episodeEntity = new EpisodeEntity();
        episodeEntity.setSeasonId(seasonEntity.getId());
        episodeEntity.setNumber(1);
        episodeEntity.setDate(getCurrentDate() - 2);
        episodeEntity.setEnqueued(true);
        episodeCrudRepository.save(episodeEntity);

        episodeEntity = new EpisodeEntity();
        episodeEntity.setSeasonId(seasonEntity.getId());
        episodeEntity.setNumber(2);
        episodeEntity.setDate(getCurrentDate() - 1);
        episodeEntity.setEnqueued(false);
        episodeCrudRepository.save(episodeEntity);

        episodeEntity = new EpisodeEntity();
        episodeEntity.setSeasonId(seasonEntity.getId());
        episodeEntity.setNumber(3);
        episodeEntity.setDate(getCurrentDate() + 1);
        episodeEntity.setEnqueued(false);
        episodeCrudRepository.save(episodeEntity);

        seasonEntity = new SeasonEntity();
        seasonEntity.setTitle("Season 2");
        seasonEntity.setWikiUrl("url");
        seasonEntity.setPage("page");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        seasonEntity = seasonCrudRepository.save(seasonEntity);
        final long id2 = seasonEntity.getId();

        episodeEntity = new EpisodeEntity();
        episodeEntity.setSeasonId(seasonEntity.getId());
        episodeEntity.setNumber(1);
        episodeEntity.setDate(getCurrentDate() - 1);
        episodeEntity.setEnqueued(false);
        episodeCrudRepository.save(episodeEntity);

        seasonEntity = new SeasonEntity();
        seasonEntity.setTitle("Season 3");
        seasonEntity.setWikiUrl("url");
        seasonEntity.setPage("page");
        seasonEntity.setSection(-1);
        seasonEntity.setTouched(-1L);
        seasonCrudRepository.save(seasonEntity);


        List<SeasonNews> seasonNews = underTest.getAllSeasonNews();


        Assert.assertEquals(2, seasonNews.size());

        Set<Integer> episodeNumbers = seasonNews.stream().filter(element -> element.getId().equals(id1)).findFirst().get().getNewEpisodes();
        Assert.assertEquals(1, episodeNumbers.size());
        Assert.assertTrue(episodeNumbers.contains(2));

        episodeNumbers = seasonNews.stream().filter(element -> element.getId().equals(id2)).findFirst().get().getNewEpisodes();
        Assert.assertEquals(1, episodeNumbers.size());
        Assert.assertTrue(episodeNumbers.contains(1));
    }

    private Integer getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return (year % 100) * 10000 + month * 100 + day;
    }

}
