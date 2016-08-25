package com.raychen518.study.hibernatesearch;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 * This class serves as the manager of manipulating the SomeObject entities.
 * Running this class demonstrates the usages of analyzers.
 * </p>
 */
public class SomeObjectManager {

    // =================================
    // Query Statements
    // =================================
    private static final String QUERY_STATEMENT_FROM_ENTITY = "from SomeObject";

    // =================================
    // Field Names
    // =================================
    private static final String FIELD_NAME_FIELD_1 = "field1";

    // =================================
    // Field Values
    // =================================
    private static final String FIELD_VALUE_FIELD_1_01 = "Abc01-Def02@Ghi03#Jkl04&Mno05(Pqr06)Stu07";

    // =================================
    // Message Texts
    // =================================
    private static final String MESSAGE_TEXT_FIELD_VALUE_LABEL = "fieldValue: ";

    // =================================
    // Misc
    // =================================
    private static final Class<?> ENTITY_TYPE = SomeObject.class;

    /**
     * Serves as the launcher of current class.
     * 
     * @param args
     *            The launch arguments.
     * @throws InterruptedException
     *             The InterruptedException exception.
     */
    public static void main(String[] args) throws InterruptedException {
        SomeObjectManager manager = new SomeObjectManager();

        manager.startIndexing();

        manager.deleteAllItems();
        manager.saveSomeItems();
        manager.listAllItems();

        manager.search1();
    }

    private void startIndexing() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.createIndexer(ENTITY_TYPE).startAndWait();
    }

    /**
     * Delete all entity items.
     */
    public void deleteAllItems() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<SomeObject> results = session.createQuery(QUERY_STATEMENT_FROM_ENTITY).list();

        for (SomeObject result : results) {
            session.delete(result);
        }

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Save some entity items.
     */
    public void saveSomeItems() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        for (int i = 0; i < 3; i++) {
            session.save(
                    new SomeObject(generateSomeObjectField1(), generateSomeObjectField2(), generateSomeObjectField3()));
        }

        // ---------------------------------------------------------------------

        session.save(new SomeObject(FIELD_VALUE_FIELD_1_01, generateSomeObjectField2(), generateSomeObjectField3()));

        session.getTransaction().commit();
        session.close();
    }

    /**
     * List all entity items.
     */
    public void listAllItems() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<SomeObject> queryResults = session.createQuery(QUERY_STATEMENT_FROM_ENTITY).list();
        System.out.println();
        for (SomeObject queryResult : queryResults) {
            System.out.println(queryResult);
        }

        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void search1() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(ENTITY_TYPE).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            String fieldValue = FIELD_VALUE_FIELD_1_01;
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_FIELD_1)
                    .matching(fieldValue).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, ENTITY_TYPE);
            System.out.println(MESSAGE_TEXT_FIELD_VALUE_LABEL + fieldValue);
            CommonsUtil.showQueryString(query);

            List<SomeObject> queryResults = query.list();
            for (SomeObject queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            String fieldValue = "def02";
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_FIELD_1)
                    .matching(fieldValue).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, ENTITY_TYPE);
            System.out.println(MESSAGE_TEXT_FIELD_VALUE_LABEL + fieldValue);
            CommonsUtil.showQueryString(query);

            List<SomeObject> queryResults = query.list();
            for (SomeObject queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            String fieldValue = "DEF02";
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_FIELD_1)
                    .matching(fieldValue).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, ENTITY_TYPE);
            System.out.println(MESSAGE_TEXT_FIELD_VALUE_LABEL + fieldValue);
            CommonsUtil.showQueryString(query);

            List<SomeObject> queryResults = query.list();
            for (SomeObject queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        fullTextSession.getTransaction().commit();
    }

    private static final String generateSomeObjectField1() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    private static final String generateSomeObjectField2() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    private static final String generateSomeObjectField3() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

}