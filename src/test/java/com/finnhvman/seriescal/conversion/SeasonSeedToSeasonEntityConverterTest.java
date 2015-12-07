package com.finnhvman.seriescal.conversion;

import com.finnhvman.seriescal.model.SeasonSeed;
import com.finnhvman.seriescal.services.store.jpa.entities.SeasonEntity;
import com.finnhvman.seriescal.services.wiki.parsers.UrlParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;

@RunWith(MockitoJUnitRunner.class)
public class SeasonSeedToSeasonEntityConverterTest {

    @InjectMocks
    private SeasonSeedToSeasonEntityConverter underTest;
    @Mock
    private UrlParser urlParser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvertOnValidSource() throws Exception {
        String url = "https://en.wikipedia.org/wiki/The_Big_Bang_Theory_%28season_9%29";
        String wikiUrl = "The_Big_Bang_Theory_%28season_9%29";
        String page = "The_Big_Bang_Theory_%28season_9%29";
        String title = "The Big Bang Theory (Season 9)";

        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl(url);

        Mockito.when(urlParser.extractWikiUrl(url)).thenReturn(wikiUrl);
        Mockito.when(urlParser.extractPage(url)).thenReturn(page);
        Mockito.when(urlParser.extractTitle(url)).thenReturn(title);


        SeasonEntity seasonEntity = underTest.convert(seasonSeed);


        Assert.assertNull(seasonEntity.getId());
        Assert.assertEquals(wikiUrl, seasonEntity.getWikiUrl());
        Assert.assertEquals(page, seasonEntity.getPage());
        Assert.assertEquals(Integer.valueOf(-1), seasonEntity.getSection());
        Assert.assertEquals(Long.valueOf(-1), seasonEntity.getTouched());
        Assert.assertEquals(title, seasonEntity.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertOnInvalidSource() throws Exception {
        SeasonSeed seasonSeed = new SeasonSeed();
        seasonSeed.setUrl("");

        Mockito.when(urlParser.extractWikiUrl("")).thenThrow(new ParseException("", 0));
        Mockito.when(urlParser.extractPage("")).thenThrow(new ParseException("", 0));
        Mockito.when(urlParser.extractTitle("")).thenThrow(new ParseException("", 0));


        underTest.convert(seasonSeed);
    }

}
