package com.raychen518.study.hibernatesearch;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

public final class CommonsUtil {

    private static final String DELIMITER_LINE = "################################################################################";

    private CommonsUtil() {
    }

    /**
     * Print a delimiter line.
     * 
     * @param needingUpperBlankLine
     *            Whether the delimiter line needs a upper blank line.
     */
    public static void printDelimiterLine(boolean needingUpperBlankLine) {
        if (needingUpperBlankLine) {
            System.out.println();
        }

        System.out.println(DELIMITER_LINE);
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
