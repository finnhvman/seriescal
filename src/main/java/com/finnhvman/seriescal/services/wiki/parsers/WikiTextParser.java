package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.finnhvman.seriescal.services.wiki.parsers.constants.ParserTokens.*;

@Component
public class WikiTextParser {

    // TODO deal with date creation, timezones, daylightsavings

    public Map<Integer, Date> extractEpisodeDates(JsonNode wikiSection) throws ParseException {
        String wikiText = extractWikiText(wikiSection);
        String[] episodeBlocks = wikiText.split(EPISODE_LIST);
        Map<Integer, Date> episodeDates = new HashMap<>();
        for (int i = 1; i < episodeBlocks.length; i++) {
            Integer episodeNumber = extractEpisodeNumber(episodeBlocks[i]);
            Date episodeDate = extractEpisodeDate(episodeBlocks[i]);
            episodeDates.put(episodeNumber, episodeDate);
        }

        // TODO
        return episodeDates;
    }

    private String extractWikiText(JsonNode wikiSection) {
        JsonNode parseNode = wikiSection.get(PARSE);
        JsonNode wikiTextNode = parseNode.get(WIKITEXT);
        JsonNode asteriskNode = wikiTextNode.get(ASTERISK);
        return asteriskNode.asText();
    }

    private Integer extractEpisodeNumber(String episodeBlock) throws ParseException {
        String[] episodeData = episodeBlock.split(ANY_WHITESPACE_CAHARCTER + Pattern.quote(PIPE));
        for (String data : episodeData) {
            if (data.contains(EPISODE_NUMBER_2)) {
                String episodeNumber = data.split(EQUAL)[1].trim();
                return extendedValueOf(episodeNumber);
            }
        }
        for (String data : episodeData) {
            if (data.contains(EPISODE_NUMBER)) {
                String episodeNumber = data.split(EQUAL)[1].trim();
                return extendedValueOf(episodeNumber);
            }
        }
        throw new ParseException("Could not parse Episode Number.", 0);
    }

    private Integer extendedValueOf(String number) {
        if (EN_DASH.equals(number)) {
            return 0;
        } else {
            return Integer.valueOf(number);
        }
    }

    private Date extractEpisodeDate(String episodeBlock) throws ParseException {
        String[] episodeData = episodeBlock.split(ANY_WHITESPACE_CAHARCTER + Pattern.quote(PIPE));
        for (String data : episodeData) {
            if (data.contains(ORIGINAL_AIR_DATE)) {
                String[] dateElements = data.split(Pattern.quote(PIPE));
                Integer year = Integer.valueOf(dateElements[1]);
                Integer month = Integer.valueOf(dateElements[2]);
                Integer day = Integer.valueOf(dateElements[3].replaceAll(ANY_NOT_NUMBER_REGEX, EMPTY));
                return new Date(year, month, day);
            }
        }
        throw new ParseException("Could not parse Original Air Date.", 0);
    }

}
