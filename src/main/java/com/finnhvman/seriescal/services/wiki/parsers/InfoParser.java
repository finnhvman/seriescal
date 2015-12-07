package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import static com.finnhvman.seriescal.services.wiki.parsers.constants.ParserTokens.*;

@Component
public class InfoParser {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public Map<String, Long> extractTouchedTimes(JsonNode info) {
        Map<String, String> normalizedMap = extractNormalizedMap(info);
        Map<String, Long> touchedMap = extractTouchedMap(info);
        return normalizedMap.keySet().parallelStream()
                .collect(Collectors.toMap(key -> key, key -> touchedMap.get(normalizedMap.get(key))));
    }

    private Map<String, String> extractNormalizedMap(JsonNode info) {
        JsonNode queryNode = info.get(QUERY);
        JsonNode normalizedNode = queryNode.get(NORMALIZED);
        return collectNormalized(normalizedNode.iterator());
    }

    private Map<String, String> collectNormalized(Iterator<JsonNode> iterator) {
        Map<String, String> normalizedMap = new HashMap<>();
        while (iterator.hasNext()) {
            JsonNode element = iterator.next();
            String from = element.get(FROM).asText();
            String to = element.get(TO).asText();
            normalizedMap.put(from, to);
        }
        return normalizedMap;
    }

    private Map<String, Long> extractTouchedMap(JsonNode info) {
        JsonNode queryNode = info.get(QUERY);
        JsonNode normalizedNode = queryNode.get(PAGES);
        return collectTouched(normalizedNode.iterator());
    }

    private Map<String, Long> collectTouched(Iterator<JsonNode> iterator) {
        Map<String, Long> touchedMap = new HashMap<>();
        while (iterator.hasNext()) {
            JsonNode element = iterator.next();
            String title = element.get(TITLE).asText();
            String touched = element.get(TOUCHED).asText();
            Long touchedTime = parseTouchedTime(touched);
            touchedMap.put(title, touchedTime);
        }
        return touchedMap;
    }

    private long parseTouchedTime(String formattedDate) {
        Date date = null;
        try {
            date = dateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

}
