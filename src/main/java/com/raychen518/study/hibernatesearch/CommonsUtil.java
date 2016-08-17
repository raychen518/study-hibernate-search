package com.raychen518.study.hibernatesearch;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

public final class CommonsUtil {

    public static final String COMPILER_WARNING_NAME_RAW_TYPES = "rawtypes";
    public static final String COMPILER_WARNING_NAME_UNCHECKED = "unchecked";
    public static final String DELIMITER_LINE_PREFIX_CONNECTOR = " - ";

    private static final String DELIMITER_LINE = "################################################################################";

    private CommonsUtil() {
    }

    /**
     * Print a delimiter line.
     */
    public static void printDelimiterLine() {
        printDelimiterLine(false, null);
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
     * Show the query string of a Hibernate Search.
     * 
     * @param query
     *            A Hibernate Search query.
     */
    public static void showQueryString(Query query) {
        System.out.println("Query String: " + query.getQueryString());
    }

    /**
     * Replace each Carriage Return or Linefeed character in the specified
     * string with a space.
     * 
     * @param string
     *            The target string.
     * @return The string changed by the replacement.
     */
    public static String replaceCrLf(String string) {
        return (string == null) ? null
                : string.replace(String.valueOf(CharUtils.CR), StringUtils.SPACE).replace(String.valueOf(CharUtils.LF),
                        StringUtils.SPACE);
    }

    public static void main(String[] args) {
    }

}
