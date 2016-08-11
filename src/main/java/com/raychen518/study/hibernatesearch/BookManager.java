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

    private static final String FIELD_NAME_BOOK_ISBN = "isbn";
    private static final String FIELD_NAME_BOOK_NAME = "name";
    private static final String FIELD_NAME_BOOK_AUTHOR_NAME = "authorName";
    private static final String FIELD_NAME_BOOK_INTRO = "intro";

    private static final String BOOK_ISBN_1 = "1231121234561";
    private static final String BOOK_ISBN_2 = "1234567890123";
    private static final String BOOK_ISBN_3 = "3210987654321";
    private static final String BOOK_NAME = "Abcdefg";
    private static final String BOOK_COMMON_VALUE = "a1b2c3";

    private static final String MESSAGE_TEXT_NORMAL = "Normal";

    public static void main(String[] args) throws InterruptedException {
        BookManager manager = new BookManager();

        manager.startIndexing();

        manager.deleteAll();
        manager.saveSome();
        manager.listAll();

        manager.searchAsKeywordQuery();
        manager.searchAsFuzzyQuery();
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

        session.save(new Book(generateBookIsbn(), BOOK_NAME, generateBookAuthorName(), generateBookPrice(),
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
        // Invoke the keyword() method on the query builder.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NORMAL);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_ISBN)
                    .matching(BOOK_ISBN_1).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Keyword Query - Searching Multiple Words on One Field
        // Join every 2 words by 1 space and use the join result as the
        // matching() method's parameter.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Searching Multiple Words on One Field");

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_ISBN)
                    .matching(BOOK_ISBN_1 + CommonsUtil.SPACE + BOOK_ISBN_2 + CommonsUtil.SPACE + BOOK_ISBN_3)
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
        // Fuzzy Query - Normal
        // Invoke the fuzzy() method on the query builder.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NORMAL);

            // By default, a Fuzzy query allows 2 changes between the 2 terms
            // involved in the search.
            // For example, here, search by the term "A1cde2g" returns the
            // document containing the term "Abcdefg", although there are 2
            // changes between these 2 terms.
            String bookName = BOOK_NAME.replace(BOOK_NAME.charAt(1), '1')
                    .replace(BOOK_NAME.charAt(BOOK_NAME.length() - 2), '2');
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
        // Fuzzy Query - Specifying Edit Distance
        // Invoke the withEditDistanceUpTo(...) method.
        // Note: The withThreshold(...) method is deprecated now and replaced
        // with the withEditDistanceUpTo(...) method.
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Specifying Edit Distance");

            String bookName = BOOK_NAME.replace(BOOK_NAME.charAt(1), '1');
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
        // Fuzzy Query - Specifying Prefix Length
        // Invoke the withPrefixLength(...) method.
        // =====================================================================
        // TODO Fuzzy Query - Specifying Prefix Length cannot be tested.
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + "Specifying Prefix Length");

            int prefixLength = 3;
            String bookName = RandomStringUtils.randomAlphanumeric(prefixLength) + BOOK_NAME;
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
    private void searchOnMultipleFields() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Query on Multiple Fields - Using One onFields() Method
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
        // Query on Multiple Fields - Using One onField() Method and Multiple
        // andField() Methods
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