package com.raychen518.study.hibernatesearch;

import java.util.List;

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
        manager.search();
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

    public void saveSome() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(new Book(1231121234561L, 1, "xxx"));
        session.save(new Book(1111111111111L, 1, "abc"));
        session.save(new Book(1111111111111L, 2, "def"));
        session.save(new Book(1111111111111L, 3, "ghi"));

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

    private void search() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("content").matching("def").createQuery();

        Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);

        @SuppressWarnings("unchecked")
        List<Book> queryResults = query.list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        fullTextSession.getTransaction().commit();
    }

}