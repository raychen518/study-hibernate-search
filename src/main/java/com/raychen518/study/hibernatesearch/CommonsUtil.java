package com.raychen518.study.hibernatesearch;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

public final class CommonsUtil {

    public static final String SPACE = " ";

    public static final String DELIMITER_LINE_PREFIX_CONNECTOR = " - ";

    public static final String COMPILER_WARNING_NAME_RAW_TYPES = "rawtypes";
    public static final String COMPILER_WARNING_NAME_UNCHECKED = "unchecked";

    private static final String DELIMITER_LINE = "################################################################################";

    private CommonsUtil() {
    }

    /**
     * Print a delimiter line.
     * 
     * @param needingUpperBlankLine
     *            Whether the delimiter line needs a upper blank line.
     * @param prefix
     *            The delimiter line's prefix. If the value is null, it means
     *            that the delimiter line has no prefix.
     */
    public static void printDelimiterLine(boolean needingUpperBlankLine, String prefix) {
        if (needingUpperBlankLine) {
            System.out.println();
        }

        if (StringUtils.isNotBlank(prefix)) {
            System.out.println(prefix);
        }

        System.out.println(DELIMITER_LINE);
    }

    /**
     * Print a delimiter line.
     */
    public static void printDelimiterLine() {
        printDelimiterLine(false, null);
    }

    /**
     * Show the query string of a Hibernate Search.
     * 
     * @param query
     *            A Hibernate Search query.
     */
    public static void showQueryString(Query query) {
        System.out.println("Query String: " + query.getQueryString());
    }

    public static Long generateRandomNumber() {
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    public static void main(String[] args) {
        System.out.println(generateRandomNumber());
        System.out.println(generateRandomNumber());
        System.out.println(generateRandomNumber());
        System.out.println(generateRandomNumber());
        System.out.println(generateRandomNumber());
        System.out.println();

        for (int i = 0; i < 15; i++) {
            System.out.println(new Random().nextLong());

        }
        System.out.println();

        for (int i = 0; i < 150; i++) {
            System.out.println(RandomUtils.nextLong(0, Long.MAX_VALUE));

        }
        System.out.println();

    }

}
