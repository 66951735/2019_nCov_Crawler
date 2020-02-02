package com.leeshengis.crawler.common;

import java.util.regex.Pattern;

/**
 * @author lisheng
 */
public class Patterns {

    public final static String STATISTICS_STRING = "\\{(\"id\".*?)\\}";

    public final static Pattern STATISTICS = Pattern.compile(STATISTICS_STRING);

    public final static String AREA_STAT_STRING = "\\[.*\\]";

    public final static Pattern AREA_STAT = Pattern.compile(AREA_STAT_STRING);
}
