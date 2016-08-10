package com.raychen518.study.hibernatesearch;

/**
 * <pre>
 * +++++++++++++++++
 * Contents
 * +++++++++++++++++
 * Versions
 * Overview
 * Steps to Use
 * References
 * 
 * =================
 * Versions
 * =================
 * Library              Version
 * -------------------------------------
 * Hibernate Search     5.5.4.Final
 * Java SE              1.8 (jdk1.8.0_66)
 * 
 * =================
 * Overview
 * =================
 * Hibernate Search is a Java framework which offers full-text search support by combining the power of Hibernate and Apache Lucene.
 * 
 * =================
 * Steps to Use
 * =================
 * 01. Introduce the Hibernate Search dependency in the POM file.
 *     pom.xml
 *     -------------------------------------------------------------------------
 *     <dependency>
 *         <groupId>org.hibernate</groupId>
 *         <artifactId>hibernate-search-orm</artifactId>
 *         <version>5.5.4.Final</version>
 *     </dependency>
 *     -------------------------------------------------------------------------
 * 
 * 02. Configure Hibernate Search to store its indexes on the file system.
 *     hibernate.cfg.xml
 *     -------------------------------------------------------------------------
 *     <property name="hibernate.search.default.directory_provider">filesystem</property>
 *     <property name="hibernate.search.default.indexBase">lucene/indexes</property>
 *     -------------------------------------------------------------------------
 * 
 * 03. Mark the entities to be indexed by Hibernate Search, using the @Indexed annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Entity
 *-    @Indexed
 *     public class Book {
 *     -------------------------------------------------------------------------
 * 
 * 04. Mark the entity fields as the entity identifiers to be used by Hibernate Search, using the @Id or @DocumentId annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Id
 *     private Long id;
 *     -------------------------------------------------------------------------
 * 
 * 05. Mark the entity fields to be indexed by Hibernate Search, using the @Field annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Field
 *     private String isbn;
 * 
 *-    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
 *     private String name;
 *     -------------------------------------------------------------------------
 * 
 * 06. (if necessary) Mark the entity associations to be indexed by Hibernate Search, using the @IndexedEmbedded annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @IndexedEmbedded
 *     private Publisher publisher;
 *     -------------------------------------------------------------------------
 * 
 * 07. (if necessary) Specify the entity fields' field bridge implementations for data encoding in Hibernate Search, using the @FieldBridge / @DateBridge / etc. annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Field
 *-    @FieldBridge(impl = BookIntroBridge.class)
 *     private String intro;
 * 
 *-    @Field
 *-    @DateBridge(resolution = Resolution.YEAR)
 *     private Date publicationDate;
 *     -------------------------------------------------------------------------
 * 
 * 08. Add the process to start the Hibernate Search indexing when the application starts.
 *     BookManager.java
 *     -------------------------------------------------------------------------
 *     public static void main(String[] args) throws InterruptedException {
 *         BookManager manager = new BookManager();
 *         manager.startIndexing();
 *         ...
 *     }
 * 
 *     private void startIndexing() throws InterruptedException {
 *         FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
 *         fullTextSession.createIndexer(Book.class).startAndWait();
 *     }
 *     -------------------------------------------------------------------------
 * 
 * 09. Use the Hibernate Search or Apache Lucene API to perform the full-text search.
 *     BookManager.java
 *     -------------------------------------------------------------------------
 *     private void search() {
 *         FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
 *         fullTextSession.beginTransaction();
 * 
 *         QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
 * 
 *         org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("content").matching("def").createQuery();
 *         Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
 * 
 *-        @SuppressWarnings("unchecked")
 *         List<Book> queryResults = query.list();
 *         for (Book queryResult : queryResults) {
 *             System.out.println(queryResult);
 *         }
 * 
 *         fullTextSession.getTransaction().commit();
 *     }
 *     -------------------------------------------------------------------------
 * 
 * =================
 * References
 * =================
 * Hibernate - Hibernate Search
 * http://hibernate.org/search/
 * 
 * Hibernate - Hibernate Search 5.5 Reference Documentation
 * http://docs.jboss.org/hibernate/search/5.5/reference/en-US/html_single/
 * 
 * Hibernate - Hibernate ORM
 * http://hibernate.org/orm/
 * 
 * Apache - Apache Lucene
 * https://lucene.apache.org/
 * </pre>
 */
public class Introduction {
}
