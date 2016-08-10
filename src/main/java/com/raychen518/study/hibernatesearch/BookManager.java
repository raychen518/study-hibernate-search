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

    public static void main(String[] args) throws InterruptedException {
        BookManager manager = new BookManager();
        manager.startIndexing();
        manager.deleteAll();
        manager.saveSome();
        manager.listAll();
        manager.searchAsKeywordQuery();
    }

    private void startIndexing() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.createIndexer(Book.class).startAndWait();
    }

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Book> results = session.createQuery("from Book").list();

        for (Book result : results) {
            session.delete(result);
        }

        session.getTransaction().commit();
        session.close();
    }

    private static final String DEFAULT_BOOK_ISBN = "1231121234561";

    public void saveSome() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        for (int i = 0; i < 3; i++) {
            session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                    generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                    generateBookPublisher(session)));
        }

        session.save(new Book(DEFAULT_BOOK_ISBN, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.getTransaction().commit();
        session.close();
    }

    public void listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Book> queryResults = session.createQuery("from Book").list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        session.getTransaction().commit();
        session.close();
    }

    private void searchAsKeywordQuery() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField("isbn").matching(DEFAULT_BOOK_ISBN).createQuery();
        Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

        CommonsUtil.printDelimiterLine(true);
        @SuppressWarnings("unchecked")
        List<Book> queryResults = query.list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }
        CommonsUtil.printDelimiterLine(false);

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
        return session.get(Publisher.class, session.save(new Publisher(RandomStringUtils.randomAlphanumeric(15),
                RandomStringUtils.randomAlphanumeric(30))));
    }

}