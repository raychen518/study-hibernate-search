package com.raychen518.study.hibernatesearch;

/**
 * <pre>
 * +++++++++++++++++
 * Contents
 * +++++++++++++++++
 * Libraries
 * Overview
 * Steps to Use
 * Analyzers
 *     Overview
 *     Use
 *     Definition
 *     Example
 * References
 * 
 * =================
 * Libraries
 * =================
 * Here is the list of all used libraries and their versions.
 * Library              Version
 * -------------------------------------
 * Hibernate Search     5.5.4.Final
 * Java SE              1.8.0_66
 * 
 * =================
 * Overview
 * =================
 * Hibernate Search is a Java framework which offers full-text search support by combining the power of Hibernate and Apache Lucene.
 * 
 * =================
 * Steps to Use
 * =================
 * Here are the normal steps to use Hibernate Search with corresponding code examples.
 * Note: These steps do not cover all approaches to use Hibernate Search.
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
 *-    @Indexed
 *     public class Book {
 *     -------------------------------------------------------------------------
 * 
 * 04. Mark the entity fields to be indexed by Hibernate Search as the entity identifiers, using the @Id or @DocumentId annotation.
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
 *-    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
 *     private String name;
 *     -------------------------------------------------------------------------
 * 
 * 06. (if necessary) Mark the entity fields to be indexed by Hibernate Search as the entity associations, using the @IndexedEmbedded annotation.
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
 * 08. (if necessary) Mark the entity fields sortable in Hibernate Search, using the @SortableField annotation.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Field
 *-    @SortableField
 *     private double price;
 *     -------------------------------------------------------------------------
 * 
 * 09. (if necessary) Define and use analyzers, using the @AnalyzerDef / @AnalyzerDefs annotation and the @Analyzer annotation respectively.
 *     Book.java
 *     -------------------------------------------------------------------------
 *-    @Indexed
 *-    @AnalyzerDef(name = "remarksAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class) , filters = {
 *-            @TokenFilterDef(factory = LowerCaseFilterFactory.class),
 *-            @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
 *-                    @Parameter(name = "language", value = "English") }) })
 *     public class Book {
 * 
 *-    @Field
 *-    @Analyzer(definition = "remarksAnalyzer")
 *     private String remarks;
 *     -------------------------------------------------------------------------
 * 
 * 10. Add the process to start the Hibernate Search indexing when the application starts.
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
 * 11. Use the Hibernate Search or Apache Lucene API to perform the full-text search.
 *     BookManager.java
 *     -------------------------------------------------------------------------
 *     private void search() {
 *         FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
 *         fullTextSession.beginTransaction();
 * 
 *         QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
 * 
 *         org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("intro").matching("computer").createQuery();
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
 * Analyzers
 * =================
 * =====================================
 * Overview
 * =====================================
 * An analyzer is an object that controls the process to convert text into single terms (words),
 * and it applies both the indexing process and the search process.
 * 
 * The default analyzer to be used is StandardAnalyzer (org.apache.lucene.analysis.standard.StandardAnalyzer),
 * that uses the following tokenizer and filters.
 * - StandardTokenizer (org.apache.lucene.analysis.standard.StandardTokenizer)
 * - StandardFilter (org.apache.lucene.analysis.standard.StandardFilter)
 * - LowerCaseFilter (org.apache.lucene.analysis.core.LowerCaseFilter)
 * - StopFilter (org.apache.lucene.analysis.core.StopFilter)
 * 
 * =====================================
 * Use
 * =====================================
 * To use an analyzer, perform any of the following actions.
 * - Setting the "hibernate.search.analyzer" property in the configuration file.
 * - Setting the @Analyzer annotation at the class level.
 * - Setting the @Analyzer annotation at the field level.
 * 
 * When using an analyzer, specify
 * - either the fully qualified class name of an analyzer
 * - or an analyzer definition defined by the @AnalyzerDef annotation.
 * 
 * =====================================
 * Definition
 * =====================================
 * To define an analyzer, use the @AnalyzerDef or @AnalyzerDefs annotation on
 * - any @Indexed class (regardless of where the analyzer is used),
 * - any parent class of an @Indexed class,
 * - or any "package-info.java" class of a package containing an @Indexed class.
 * Note: Analyzer definitions are global and their names must be unique.
 * 
 * Generally, defining an analyzer starts with a tokenizer followed by some filters.
 * 
 * =====================================
 * Example
 * =====================================
 * Code
 * ---------------------------------------------------------------------------------------------------------------------
 *-@Indexed
 *-@AnalyzerDef(name = "customAnalyzer",
 *     tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
 *     filters = {
 *-        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
 *-        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
 *-            @Parameter(name = "language", value = "English")
 *         })
 *     }
 * )
 * public class Book {
 *     ...
 * 
 *-    @Field
 *-    @Analyzer(definition = "customAnalyzer")
 *     private String title;
 * 
 *-    @Field
 *-    @Analyzer(definition = "customAnalyzer")
 *     private String subtitle;
 * 
 *     ...
 * }
 * ---------------------------------------------------------------------------------------------------------------------
 * 
 * Description
 * ---------------------------------------------------------------------------------------------------------------------
 * - An analyzer named "customAnalyzer" is defined on the class "Book".
 * - The analyzer named "customAnalyzer" is used on the fields "title" and "subtitle".
 * - The analyzer named "customAnalyzer" is using the class "StandardTokenizerFactory" as its tokenizer.
 *   Note: The "StandardTokenizerFactory" tokenizer splits words at punctuation characters and hyphens, and it is a good general purpose tokenizer.
 *         But for email addresses or Internet host names, it is not the best fit because it will split them up.
 *         In that case, either use Lucene's ClassicTokenizerFactory or implement a custom tokenizer.
 *   Note: Here are some or all punctuation characters mentioned above.
 *         They count 32 and do not include the underscore (_).
 *         ~`!@#$%^&*()-+={[}]|\:;"'<,>.?/
 * - The analyzer is using the classes "LowerCaseFilterFactory" and "SnowballPorterFilterFactory" (with a parameter "language:English") as its filters.
 *   Note: The "LowerCaseFilterFactory" filter makes the letters in each token lower case,
 *         while the "SnowballPorterFilterFactory" filter applies language specific stemming.
 * ---------------------------------------------------------------------------------------------------------------------
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
public interface Introduction {
}
