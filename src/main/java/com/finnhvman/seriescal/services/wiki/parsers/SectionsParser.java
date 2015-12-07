package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

import static com.finnhvman.seriescal.services.wiki.parsers.constants.ParserTokens.*;

@Component
public class SectionsParser {

    public Integer extractSectionNumber(JsonNode sections, String seasonNumber) throws ParseException {
        Map<String, Integer> sectionsList = extractSectionsList(sections);
        for (Map.Entry<String, Integer> section : sectionsList.entrySet()) {
            if (matchNumberedSection(section.getKey(), seasonNumber)) {
                return section.getValue();
            }
        }
        for (Map.Entry<String, Integer> section : sectionsList.entrySet()) {
            if (section.getKey().matches(EPISODES)) {
                return section.getValue();
            }
        }
        throw new ParseException("Section not found.", 0);
    }

    private Map<String, Integer>  extractSectionsList(JsonNode sections) {
        JsonNode parseNode = sections.get(PARSE);
        JsonNode sectionsNode = parseNode.get(SECTIONS);
        return collectSections(sectionsNode.iterator());
    }

    private Map<String, Integer> collectSections(Iterator<JsonNode> iterator) {
        Map<String, Integer> sectionsMap = new HashMap<>();
        while (iterator.hasNext()) {
            JsonNode element = iterator.next();
            sectionsMap.put(element.get(LINE).asText(), element.get(INDEX).asInt());
        }
        return sectionsMap;
    }

    private boolean matchNumberedSection(String section, String seasonNumber) {
        return section.matches(SERIES + SPACE + seasonNumber + ANY_CHARACTER_REGEX)
                || section.matches(SEASON + SPACE + seasonNumber + ANY_CHARACTER_REGEX);
    }

}
