package com.finnhvman.seriescal.services.wiki.parsers;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.regex.Pattern;

import static com.finnhvman.seriescal.services.wiki.parsers.constants.ParserTokens.*;

@Component
public class UrlParser {

    public String extractWikiUrl(String url) throws ParseException {
        String[] pathParts = url.split(SLASH);
        String wikiUrl = pathParts[pathParts.length - 1];
        return returnOrThrowOnEmpty(wikiUrl);
    }

    public String extractPage(String url) throws ParseException {
        String normalized = url.replaceAll(PARENTHESES[2], PARENTHESES[0]); // replace to normal parentheses
        normalized = normalized.replaceAll(PARENTHESES[3], PARENTHESES[1]);
        String page = extractWikiUrl(normalized).split(HASH_MARK)[0];
        return returnOrThrowOnEmpty(page);
    }

    public String extractSeriesTitle(String url) throws ParseException {
        String page = extractPage(url);
        String normalizedPage = page.replaceAll(UNDERSCORE, SPACE);
        if (normalizedPage.startsWith(LIST_OF_)) {
            String seriesTitle = normalizedPage
                    .replace(LIST_OF_, EMPTY)
                    .replace(_EPISODES, EMPTY);
            return returnOrThrowOnEmpty(seriesTitle);
        } else {
            String seriesTitle = normalizedPage
                    .split(Pattern.quote(PARENTHESES[0]))[0]
                    .split(Pattern.quote(PARENTHESES[2]))[0]
                    .trim();
            return returnOrThrowOnEmpty(seriesTitle);
        }
    }

    public String extractSeasonNumber(String url) {
        String[] parts = url.split(EASON_);
        if (parts.length == 2) {
            String replaced = parts[1].replaceAll(ANY_NOT_NUMBER_REGEX, SPACE);
            return replaced.split(SPACE)[0];
        }

        return "1";
    }

    public String extractTitle(String url) throws ParseException {
        return extractSeriesTitle(url) + " (Season " + extractSeasonNumber(url) + ")";
    }

    private String returnOrThrowOnEmpty(String string) throws ParseException {
        if (string.isEmpty()) {
            throw new ParseException("The result of the parse is empty. The specified url is invalid.", 0);
        }
        return string;
    }

}