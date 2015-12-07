package com.finnhvman.seriescal.services.wiki.parsers.constants;

public class ParserTokens {

    // Special characters
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String SLASH = "/";
    public static final String PIPE = "|";
    public static final String HASH_MARK = "#";
    public static final String EN_DASH = "\u2013";
    public static final String UNDERSCORE = "_";
    public static final String ASTERISK = "*";
    public static final String EQUAL = "=";
    public static final String[] PARENTHESES = new String[]{"(", ")", "%28", "%29", ".28", ".29"};

    // Regex
    public static final String ANY_CHARACTER_REGEX = ".+";
    public static final String ANY_NOT_NUMBER_REGEX = "[^0-9]+";
    public static final String ANY_WHITESPACE_CAHARCTER = "\\s";

    // Query Parameters
    public static final String QUERY = "query";
    public static final String PARSE = "parse";
    public static final String NORMALIZED = "normalized";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String PAGES = "pages";
    public static final String TITLE = "title";
    public static final String TOUCHED = "touched";
    public static final String SECTIONS = "sections";
    public static final String INDEX = "index";
    public static final String LINE = "line";
    public static final String WIKITEXT = "wikitext";

    // Other
    public static final String EPISODES = "Episodes";
    public static final String SEASON = "Season";
    public static final String SERIES = "Series";
    public static final String EPISODE_LIST = "Episode list";
    public static final String EPISODE_NUMBER = "EpisodeNumber";
    public static final String EPISODE_NUMBER_2 = "EpisodeNumber2";
    public static final String ORIGINAL_AIR_DATE = "OriginalAirDate";
    public static final String LIST_OF_ = "List of ";
    public static final String _EPISODES = " episodes";
    public static final String EASON_ = "eason_";

}
