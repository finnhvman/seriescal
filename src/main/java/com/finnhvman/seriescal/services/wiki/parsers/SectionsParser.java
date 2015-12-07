package com.finnhvman.seriescal.services.wiki.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.finnhvman.seriescal.services.wiki.parsers.constants.ParserTokens.*;

@Component
public class SectionsParser {

    public Integer extractSectionNumber(JsonNode sections, String seasonNumber) {
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
        return -1;
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
        if (section.matches(SERIES + SPACE + seasonNumber + ANY_CHARACTER_REGEX)) {
            return true;
        } else if (section.matches(SEASON + SPACE + seasonNumber + ANY_CHARACTER_REGEX)) {
            return true;
        } else {
            return false;
        }
    }

}
