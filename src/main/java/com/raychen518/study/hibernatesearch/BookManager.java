package com.raychen518.study.hibernatesearch;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

public class BookManager {

    // =================================
    // Field Names
    // =================================
    private static final String FIELD_NAME_BOOK_ISBN = "isbn";
    private static final String FIELD_NAME_BOOK_NAME = "name";
    private static final String FIELD_NAME_BOOK_AUTHOR_NAME = "authorName";
    private static final String FIELD_NAME_BOOK_PRICE = "price";
    private static final String FIELD_NAME_BOOK_INTRO = "intro";

    // =================================
    // Test Values
    // =================================
    private static final String BOOK_ISBN_01 = "1231121234561";
    private static final String BOOK_ISBN_02 = "1234567890123";
    private static final String BOOK_ISBN_03 = "3210987654321";
    private static final String BOOK_NAME_01 = "Abcdefg";
    private static final String BOOK_NAME_02 = "abc";
    private static final String BOOK_NAME_03 = "def";
    private static final String BOOK_NAME_04 = "ghi";
    private static final String BOOK_NAME_21 = "xax";
    private static final String BOOK_NAME_22 = "xbx";
    private static final String BOOK_NAME_31 = "xxxabc";
    private static final String BOOK_NAME_32 = "xxxdef";
    private static final String BOOK_NAME_33 = "xxxghi";
    private static final String BOOK_NAME_41 = "x1x2xabc";
    private static final String BOOK_NAME_42 = "x3x4xdef";
    private static final String BOOK_NAME_43 = "x5x6xghi";
    private static final String BOOK_NAME_51 = "Abc Def Ghi";
    private static final String BOOK_NAME_52 = "Abc aaa Def Ghi";
    private static final String BOOK_NAME_53 = "Abc aaa Def bbb Ghi";
    private static final String BOOK_NAME_54 = "Abc aaa Def bbb ccc Ghi";
    private static final String BOOK_AUTHOR_NAME_01 = generateBookAuthorName();
    private static final double BOOK_PRICE_01 = 80.0;
    private static final double BOOK_PRICE_02 = 85.0;
    private static final double BOOK_PRICE_03 = 90.0;
    private static final double BOOK_PRICE_04 = 95.0;
    private static final double BOOK_PRICE_11 = generateBookPrice();
    private static final String BOOK_INTRO_01 = "XXXXX\r\nXXX key=\"itemA\" value=\"valueA1\" XXX\r\nXXXXX\r\nXXX key=\"itemA\" value=\"valueA2\" XXX\r\nXXXXX\r\nXXX key=\"itemB\" value=\"valueB1\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_INTRO_02 = "XXXXX\r\nXXX key=\"itemA\" value=\"valueA1\" XXX\r\nXXXXX\r\nXXX key=\"itemB\" value=\"valueB2\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC1\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_INTRO_03 = "XXXXX\r\nXXX key=\"itemB\" value=\"valueB3\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC1\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC3\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_COMMON_VALUE = "a1b2c3";

    // =================================
    // Index Names
    // =================================
    private static final String INDEX_NAME_A = "itemA";
    private static final String INDEX_NAME_B = "itemB";
    private static final String INDEX_NAME_C = "itemC";

    // =================================
    // Index Values
    // =================================
    private static final String INDEX_VALUE_A1 = "valueA1";
    private static final String INDEX_VALUE_A2 = "valueA2";
    private static final String INDEX_VALUE_A3 = "valueA3";
    private static final String INDEX_VALUE_B1 = "valueB1";
    private static final String INDEX_VALUE_B2 = "valueB2";
    private static final String INDEX_VALUE_B3 = "valueB3";
    private static final String INDEX_VALUE_C1 = "valueC1";
    private static final String INDEX_VALUE_C2 = "valueC2";
    private static final String INDEX_VALUE_C3 = "valueC3";

    // =================================
    // Message Texts
    // =================================
    private static final String MESSAGE_TEXT_NORMAL = "Normal";
    private static final String MESSAGE_TEXT_MISC = "Misc";

    public static void main(String[] args) throws InterruptedException {
        BookManager manager = new BookManager();

        manager.startIndexing();

        manager.deleteAll();
        manager.saveSome();
        manager.listAll();

        manager.searchAsKeywordQuery();
        manager.searchAsFuzzyQuery();
        manager.searchAsWildcardQuery();
        manager.searchAsPhraseQuery();
        manager.searchAsRangeQuery();

        manager.searchOnMultipleFields();
        manager.searchUsingCombinedQueries();
        manager.searchByCustomIndexes();
    }

    private void startIndexing() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.createIndexer(Book.class).startAndWait();
    }

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<Book> results = session.createQuery("from Book").list();

        for (Book result : results) {
            session.delete(result);
        }

        session.getTransaction().commit();
        session.close();
    }

    public void saveSome() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        for (int i = 0; i < 3; i++) {
            session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                    generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                    generateBookPublisher(session)));
        }

        session.save(new Book(BOOK_ISBN_01, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_02, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_03, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_01, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_02, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_03, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_04, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_21, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_22, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_31, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_32, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_33, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_41, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_42, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_43, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_51, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_52, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_53, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_54, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_AUTHOR_NAME_01, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_01,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_02,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_03,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_04,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_11,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_01, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_02, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_03, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_COMMON_VALUE, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_COMMON_VALUE, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_COMMON_VALUE, generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_AUTHOR_NAME_01, BOOK_PRICE_11,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.getTransaction().commit();
        session.close();
    }

    public void listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<Book> queryResults = session.createQuery("from Book").list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchAsKeywordQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Normal
        // Invoke the keyword() method.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NORMAL);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_ISBN)
                    .matching(BOOK_ISBN_01).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Searching Multiple Words on One Field
        // Join every 2 words by 1 space and use the join result as the
        // matching() method's parameter.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Searching Multiple Words on One Field");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_ISBN)
                    .matching(BOOK_ISBN_01 + StringUtils.SPACE + BOOK_ISBN_02 + StringUtils.SPACE + BOOK_ISBN_03)
                    .createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchAsFuzzyQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Normal
        // Invoke the fuzzy() method.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NORMAL);

            // By default, a Fuzzy query allows 2 changes between the 2 terms
            // involved in the search.
            // For example, here, search by the term "A1cde2g" returns the
            // document containing the term "Abcdefg", although there are 2
            // changes between these 2 terms.
            String bookName = BOOK_NAME_01.replace(BOOK_NAME_01.charAt(1), '1')
                    .replace(BOOK_NAME_01.charAt(BOOK_NAME_01.length() - 2), '2');
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().fuzzy().onField(FIELD_NAME_BOOK_NAME)
                    .matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Specifying Edit Distance
        // Invoke the withEditDistanceUpTo(...) method.
        // Note: The withThreshold(...) method is deprecated now and replaced
        // with the withEditDistanceUpTo(...) method.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Specifying Edit Distance");

            String bookName = BOOK_NAME_01.replace(BOOK_NAME_01.charAt(1), '1');
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().fuzzy().withEditDistanceUpTo(1)
                    .onField(FIELD_NAME_BOOK_NAME).matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Specifying Prefix Length
        // Invoke the withPrefixLength(...) method.
        // =====================================================================
        // TODO Specifying Prefix Length cannot be tested.
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Specifying Prefix Length");

            int prefixLength = 3;
            String bookName = RandomStringUtils.randomAlphanumeric(prefixLength) + BOOK_NAME_01;
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().fuzzy().withPrefixLength(prefixLength)
                    .onField(FIELD_NAME_BOOK_NAME).matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchAsWildcardQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Using the Question Mark (?)
        // Invoke the wildcard() method.
        // Note: Wildcard queries do not apply the analyzer on the matching
        // terms because the risk of "?" or "*" being mangled is too high.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the Question Mark (?)");

            String bookName = "x?x";
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().wildcard().onField(FIELD_NAME_BOOK_NAME)
                    .matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the Asterisk Mark (*)
        // <Same as Above>
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the Asterisk Mark (*)");

            String bookName = "xxx*";
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().wildcard().onField(FIELD_NAME_BOOK_NAME)
                    .matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Misc
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_MISC);

            String bookName = "x?x?x*";
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().wildcard().onField(FIELD_NAME_BOOK_NAME)
                    .matching(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchAsPhraseQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Normal
        // Invoke the phrase() and sentence(...) methods.
        // This kind of query is used to search exact or approximate sentences.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NORMAL);

            String bookName = BOOK_NAME_51;
            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_BOOK_NAME)
                    .sentence(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Specifying Slop
        // By specifying the slop, the approximate sentences are also searched.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Specifying Slop");

            String bookName = BOOK_NAME_51;
            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().withSlop(2).onField(FIELD_NAME_BOOK_NAME)
                    .sentence(bookName).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchAsRangeQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Using the From-To Range
        // Invoke the range(), from(...) and to(...) methods.
        // Note: The From and To values are both included.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the From-To Range");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.range().onField(FIELD_NAME_BOOK_PRICE).from(80.0D)
                    .to(90.0D).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the Above Range
        // Invoke the range() and above(...) methods.
        // Note: The Above value is included.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the Above Range");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.range().onField(FIELD_NAME_BOOK_PRICE)
                    .above(80.0D).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the Below Range
        // Invoke the range() and below(...) methods.
        // Note: The Below value is included.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the Below Range");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.range().onField(FIELD_NAME_BOOK_PRICE)
                    .below(90.0D).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchOnMultipleFields() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Using One onFields() Method
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using One onFields() Method");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword()
                    .onFields(FIELD_NAME_BOOK_NAME, FIELD_NAME_BOOK_AUTHOR_NAME, FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using One onField() Method and Multiple andField() Methods
        // This approach is used when one field should be treated differently
        // from other fields (such as setting a different boost value).
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + "Using One onField() Method and Multiple andField() Methods");

            // Here, the 2nd field is treated differently by performing an
            // additional setting.
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .andField(FIELD_NAME_BOOK_AUTHOR_NAME).boostedTo(5).andField(FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchUsingCombinedQueries() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Using the must(...) Method for an AND Query
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + "Using the must(...) Method for an AND Query");

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_01).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_PRICE)
                    .matching(BOOK_PRICE_11).createQuery();
            org.apache.lucene.search.Query luceneQuery = queryBuilder.bool().must(luceneSubquery1).must(luceneSubquery2)
                    .createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the should(...) Method for an OR Query
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + "Using the should(...) Method for an OR Query");

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_01).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_PRICE)
                    .matching(BOOK_PRICE_11).createQuery();
            org.apache.lucene.search.Query luceneQuery = queryBuilder.bool().should(luceneSubquery1)
                    .should(luceneSubquery2).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the not() Method for a NOT Query
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the not() Method for a NOT Query");

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_01).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_PRICE)
                    .matching(BOOK_PRICE_11).createQuery();
            org.apache.lucene.search.Query luceneQuery = queryBuilder.bool().must(luceneSubquery1).must(luceneSubquery2)
                    .not().createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the all() Method for an ALL Query
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using the all() Method for an ALL Query");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.all().createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Using the except(...) Method to Exclude Results
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + "Using the except(...) Method to Exclude Results");

            org.apache.lucene.search.Query luceneSubquery = queryBuilder.range().onField(FIELD_NAME_BOOK_PRICE)
                    .below(BOOK_PRICE_02).createQuery();
            org.apache.lucene.search.Query luceneQuery = queryBuilder.all().except(luceneSubquery).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    /**
     * <pre>
     * Custom Indexes per Test Values
     *          Test Document
     * Index    #1                      #2                      #3
     * -------------------------------------------------------------------------
     * itemA    valueA1 valueA2         valueA1                 ----
     * itemB    valueB1                 valueB2                 valueB3
     * itemC    ----                    valueC1                 valueC1 valueC3
     * </pre>
     */
    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchByCustomIndexes() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_A)
                    .sentence(INDEX_VALUE_A1).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_A)
                    .sentence(INDEX_VALUE_A2).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_A)
                    .sentence(INDEX_VALUE_A3).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_B)
                    .sentence(INDEX_VALUE_B1).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_B)
                    .sentence(INDEX_VALUE_B2).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_B)
                    .sentence(INDEX_VALUE_B3).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_C)
                    .sentence(INDEX_VALUE_C1).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_C)
                    .sentence(INDEX_VALUE_C2).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(INDEX_NAME_C)
                    .sentence(INDEX_VALUE_C3).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }
    }

    private static final String generateBookIsbn() {
        return RandomStringUtils.randomNumeric(13);
    }

    private static final String generateBookName() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private static final String generateBookAuthorName() {
        return RandomStringUtils.randomAlphanumeric(7);
    }

    private static final double generateBookPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(decimalFormat.format(RandomUtils.nextDouble(5.0, 75.0)));
    }

    private static final String generateBookIntro() {
        return RandomStringUtils.randomAlphanumeric(50);
    }

    private static final Date generateBookPublicationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1985);
        Long minBookPublicationDateValue = calendar.getTimeInMillis();
        calendar.set(Calendar.YEAR, 2015);
        Long maxBookPublicationDateValue = calendar.getTimeInMillis();
        return new Date(RandomUtils.nextLong(minBookPublicationDateValue, maxBookPublicationDateValue));
    }

    private static final boolean generateBookAwarded() {
        return new Random().nextBoolean();
    }

    private static final Publisher generateBookPublisher(Session session) {
        return session.get(Publisher.class, session.save(
                new Publisher(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(30))));
    }

}