package com.raychen518.study.hibernatesearch;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

public class BookManager {

    private static final String FIELD_NAME_ISBN = "isbn";
    private static final String FIELD_NAME_NAME = "name";
    private static final String FIELD_NAME_AUTHOR_NAME = "authorName";
    private static final String FIELD_NAME_INTRO = "intro";

    private static final String BOOK_ISBN_1 = "1231121234561";
    private static final String BOOK_ISBN_2 = "1234567890123";
    private static final String BOOK_ISBN_3 = "3210987654321";
    private static final String BOOK_COMMON_VALUE = "a1b2c3";

    public static void main(String[] args) throws InterruptedException {
        BookManager manager = new BookManager();

        manager.startIndexing();

        manager.deleteAll();
        manager.saveSome();
        manager.listAll();

        manager.searchAsKeywordQuery();
        manager.searchOnMultipleFields();
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

        session.save(new Book(BOOK_ISBN_1, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_2, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_3, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_COMMON_VALUE, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_COMMON_VALUE, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_COMMON_VALUE, generateBookPublicationDate(), generateBookAwarded(),
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
        // Keyword Query - Normal
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Normal");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_ISBN)
                    .matching(BOOK_ISBN_1).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Keyword Query - Searching Multiple Words on One Field
        // Join the words using the Space characters, and invoke the "matching"
        // method with the join result as the parameter.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Searching Multiple Words on One Field");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_ISBN)
                    .matching(BOOK_ISBN_1 + CommonsUtil.SPACE + BOOK_ISBN_2 + CommonsUtil.SPACE + BOOK_ISBN_3)
                    .createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

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
        // Query on Multiple Fields - Using One "onFields" Method
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Using One \"onFields\" Method");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword()
                    .onFields(FIELD_NAME_NAME, FIELD_NAME_AUTHOR_NAME, FIELD_NAME_INTRO).matching(BOOK_COMMON_VALUE)
                    .createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Query on Multiple Fields - Using One "onField" Method and Multiple
        // "andField" Methods
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + "Using One \"onField\" Method and Multiple \"andField\" Methods");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_NAME)
                    .andField(FIELD_NAME_AUTHOR_NAME).andField(FIELD_NAME_INTRO).matching(BOOK_COMMON_VALUE)
                    .createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
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