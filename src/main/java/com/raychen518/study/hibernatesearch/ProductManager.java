package com.raychen518.study.hibernatesearch;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

public class ProductManager {

    public static void main(String[] args) throws InterruptedException {
        ProductManager manager = new ProductManager();
        manager.startIndexing();
        manager.deleteAll();
        manager.saveSome();
        manager.listAll();
        manager.search();
    }

    private void startIndexing() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.createIndexer(ProductA.class).startAndWait();
    }

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Product> results = session.createQuery("from Product").list();

        for (Product result : results) {
            session.delete(result);
        }

        session.getTransaction().commit();
        session.close();
    }

    public void saveSome() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Date createdDate = new Date();

        session.save(new ProductA(CommonsUtil.generateRandomNumber(), "feature001", "featureA1001", "featureA2001",
                createdDate));
        session.save(new ProductA(CommonsUtil.generateRandomNumber(), "feature002", "featureA1002", "featureA2002",
                createdDate));
        session.save(new ProductA(CommonsUtil.generateRandomNumber(), "feature003", "featureA1003", "featureA2003",
                createdDate));

        session.save(new ProductB(CommonsUtil.generateRandomNumber(), "feature001", "featureB1001", "featureB2001",
                createdDate));
        session.save(new ProductB(CommonsUtil.generateRandomNumber(), "feature002", "featureB1002", "featureB2002",
                createdDate));
        session.save(new ProductB(CommonsUtil.generateRandomNumber(), "feature003", "featureB1003", "featureB2003",
                createdDate));

        session.save(new ProductC(CommonsUtil.generateRandomNumber(), "feature001", "featureC1001", "featureC2001",
                createdDate));
        session.save(new ProductC(CommonsUtil.generateRandomNumber(), "feature002", "featureC1002", "featureC2002",
                createdDate));
        session.save(new ProductC(CommonsUtil.generateRandomNumber(), "feature003", "featureC1003", "featureC2003",
                createdDate));

        session.getTransaction().commit();
        session.close();
    }

    public void listAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings("unchecked")
        List<Product> queryResults = session.createQuery("from Product").list();
        for (Product queryResult : queryResults) {
            System.out.println(queryResult);
        }

        session.getTransaction().commit();
        session.close();
    }

    private void search() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(ProductB.class).get();
        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("feature").matching("feature002").createQuery();

        // If the 2nd parameter of the following method invocation is "ProductB.class", then the results of the type ProductB return.
        // If the 2nd parameter of the following method invocation is "Product.class", then the results of the types ProductA, ProductB and ProductC return.
        Query query = fullTextSession.createFullTextQuery(luceneQuery, ProductB.class);

        @SuppressWarnings("unchecked")
        List<Product> queryResults = query.list();
        for (Product queryResult : queryResults) {
            System.out.println("queryResult: " + queryResult);
        }

        fullTextSession.getTransaction().commit();
    }

}