package com.finnhvman.seriescal.services.wiki.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.finnhvman.seriescal.services.wiki.rest.constants.QueryParameters.*;

@Component
public class WikiRestTemplate {

    private static final String WIKIPEDIA_API = "https://en.wikipedia.org/w/api.php";

    private RestTemplate restTemplate = new RestTemplate();

    public JsonNode queryInfo(String pipedPages) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WIKIPEDIA_API)
                .queryParam(FORMAT, JSON)
                .queryParam(ACTION, QUERY)
                .queryParam(TITLES, pipedPages) // With parse action it is called "page"
                .queryParam(PROP, INFO);
        String uriString = builder.build().toUriString();

        HttpEntity<JsonNode> response = restTemplate.exchange(uriString, HttpMethod.GET, null, JsonNode.class);
        return response.getBody();
    }

    public JsonNode parseSections(String page) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WIKIPEDIA_API)
                .queryParam(FORMAT, JSON)
                .queryParam(ACTION, PARSE)
                .queryParam(PAGE, page)
                .queryParam(PROP, SECTIONS);
        String uriString = builder.build().toUriString();

        HttpEntity<JsonNode> response = restTemplate.exchange(uriString, HttpMethod.GET, null, JsonNode.class);
        return response.getBody();
    }

    public JsonNode parseSection(String page, int section) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WIKIPEDIA_API)
                .queryParam(FORMAT, JSON)
                .queryParam(ACTION, PARSE)
                .queryParam(PAGE, page)
                .queryParam(SECTION, section)
                .queryParam(PROP, WIKITEXT);
        String uriString = builder.build().toUriString();

        HttpEntity<JsonNode> response = restTemplate.exchange(uriString, HttpMethod.GET, null, JsonNode.class);
        return response.getBody();
    }

}
