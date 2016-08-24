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
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 * This class serves as the manager of manipulating the Book entities.
 */
public class BookManager {

    // =================================
    // Query Statements
    // =================================
    private static final String QUERY_STATEMENT_FROM_BOOK = "from Book";

    // =================================
    // Field Names
    // =================================
    private static final String FIELD_NAME_BOOK_ISBN = "isbn";
    private static final String FIELD_NAME_BOOK_NAME = "name";
    private static final String FIELD_NAME_BOOK_AUTHOR_NAME = "authorName";
    private static final String FIELD_NAME_BOOK_PRICE = "price";
    private static final String FIELD_NAME_BOOK_INTRO = "intro";
    private static final String FIELD_NAME_BOOK_AWARDED = "awarded";
    private static final String FIELD_NAME_BOOK_REMARKS = "remarks";

    private static final String FIELD_NAME_A = "itemA";
    private static final String FIELD_NAME_B = "itemB";
    private static final String FIELD_NAME_C = "itemC";

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
    private static final String BOOK_NAME_61 = generateBookName();
    private static final String BOOK_AUTHOR_NAME_01 = generateBookAuthorName();
    private static final String BOOK_AUTHOR_NAME_02 = generateBookAuthorName();
    private static final double BOOK_PRICE_01 = 80.0;
    private static final double BOOK_PRICE_02 = 85.0;
    private static final double BOOK_PRICE_03 = 90.0;
    private static final double BOOK_PRICE_04 = 95.0;
    private static final double BOOK_PRICE_11 = generateBookPrice();
    private static final double BOOK_PRICE_21 = generateBookPrice();
    private static final String BOOK_INTRO_01 = "XXXXX\r\nXXX key=\"itemA\" value=\"valueA1\" XXX\r\nXXXXX\r\nXXX key=\"itemA\" value=\"valueA2\" XXX\r\nXXXXX\r\nXXX key=\"itemB\" value=\"valueB1\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_INTRO_02 = "XXXXX\r\nXXX key=\"itemA\" value=\"valueA1\" XXX\r\nXXXXX\r\nXXX key=\"itemB\" value=\"valueB2\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC1\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_INTRO_03 = "XXXXX\r\nXXX key=\"itemB\" value=\"valueB3\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC1\" XXX\r\nXXXXX\r\nXXX key=\"itemC\" value=\"valueC3\" XXX\r\nXXXXX\r\n";
    private static final String BOOK_REMARKS_01 = "a01~b02`c03!d04@e05#f06$g07%h08^i09&j10*k11(l12)m13_n14-o15+p16=q17{r18[s19}t20]u21|v22\\w23:x24;y25\"z26'A27<B28,C29>D30.E31?F32/G33";
    private static final String BOOK_REMARKS_02 = "xxx do xxx";
    private static final String BOOK_REMARKS_03 = "xxx does xxx";
    private static final String BOOK_REMARKS_04 = "xxx did xxx";
    private static final String BOOK_REMARKS_05 = "xxx doing xxx";
    private static final String BOOK_REMARKS_06 = "xxx refactor xxx";
    private static final String BOOK_REMARKS_07 = "xxx refactors xxx";
    private static final String BOOK_REMARKS_08 = "xxx refactored xxx";
    private static final String BOOK_REMARKS_09 = "xxx refactoring xxx";
    private static final String BOOK_COMMON_VALUE_01 = RandomStringUtils.randomAlphabetic(5);
    private static final String BOOK_COMMON_VALUE_11 = RandomStringUtils.randomAlphabetic(5);

    private static final String VALUE_A1 = "valueA1";
    private static final String VALUE_A2 = "valueA2";
    private static final String VALUE_A3 = "valueA3";
    private static final String VALUE_B1 = "valueB1";
    private static final String VALUE_B2 = "valueB2";
    private static final String VALUE_B3 = "valueB3";
    private static final String VALUE_C1 = "valueC1";
    private static final String VALUE_C2 = "valueC2";
    private static final String VALUE_C3 = "valueC3";

    // =================================
    // Message Texts
    // =================================
    private static final String MESSAGE_TEXT_NORMAL = "Normal";
    private static final String MESSAGE_TEXT_SEARCHING_MULTIPLE_WORDS_ON_ONE_FIELD = "Searching Multiple Words on One Field";
    private static final String MESSAGE_TEXT_SPECIFYING_EDIT_DISTANCE = "Specifying Edit Distance";
    private static final String MESSAGE_TEXT_SPECIFYING_PREFIX_LENGTH = "Specifying Prefix Length";
    private static final String MESSAGE_TEXT_USING_THE_QUESTION_MARK = "Using the Question Mark (?)";
    private static final String MESSAGE_TEXT_USING_THE_ASTERISK_MARK = "Using the Asterisk Mark (*)";
    private static final String MESSAGE_TEXT_MISC = "Misc";
    private static final String MESSAGE_TEXT_SPECIFYING_SLOP = "Specifying Slop";
    private static final String MESSAGE_TEXT_USING_THE_FROM_TO_RANGE = "Using the From-To Range";
    private static final String MESSAGE_TEXT_USING_THE_ABOVE_RANGE = "Using the Above Range";
    private static final String MESSAGE_TEXT_USING_THE_BELOW_RANGE = "Using the Below Range";
    private static final String MESSAGE_TEXT_USING_ONE_ONFIELDS_METHOD = "Using One onFields() Method";
    private static final String MESSAGE_TEXT_USING_ONE_ONFIELD_METHOD_AND_MULTIPLE_ANDFIELD_METHODS = "Using One onField() Method and Multiple andField() Methods";
    private static final String MESSAGE_TEXT_USING_THE_MUST_METHOD_FOR_AN_AND_QUERY = "Using the must(...) Method for an AND Query";
    private static final String MESSAGE_TEXT_USING_THE_SHOULD_METHOD_FOR_AN_OR_QUERY = "Using the should(...) Method for an OR Query";
    private static final String MESSAGE_TEXT_USING_THE_NOT_METHOD_FOR_A_NOT_QUERY = "Using the not() Method for a NOT Query";
    private static final String MESSAGE_TEXT_USING_THE_ALL_METHOD_FOR_AN_ALL_QUERY = "Using the all() Method for an ALL Query";
    private static final String MESSAGE_TEXT_USING_THE_EXCEPT_METHOD_TO_EXCLUDE_RESULTS = "Using the except(...) Method to Exclude Results";
    private static final String MESSAGE_TEXT_SEARCHING_ALL_BOOKS_AWARDED = "Searching All Books Awarded";
    private static final String MESSAGE_TEXT_NO_TESTING = "No Testing";
    private static final String MESSAGE_TEXT_TESTING_THE_STANDARD_TOKENIZER_1 = "Testing the Standard Tokenizer - 1";
    private static final String MESSAGE_TEXT_TESTING_THE_STANDARD_TOKENIZER_2 = "Testing the Standard Tokenizer - 2";
    private static final String MESSAGE_TEXT_TESTING_THE_LOWER_CASE_FILTER = "Testing the Lower Case Filter";
    private static final String MESSAGE_TEXT_TESTING_THE_SNOWBALL_PORTER_FILTER_1 = "Testing the Snowball Porter Filter - 1";
    private static final String MESSAGE_TEXT_TESTING_THE_SNOWBALL_PORTER_FILTER_2 = "Testing the Snowball Porter Filter - 2";
    private static final String MESSAGE_TEXT_QUERIES_NOT_BOOSTED = "Queries Not Boosted";
    private static final String MESSAGE_TEXT_QUERIES_BOOSTED = "Queries Boosted";
    private static final String MESSAGE_TEXT_FIELDS_NOT_BOOSTED = "Fields Not Boosted";
    private static final String MESSAGE_TEXT_FIELDS_BOOSTED = "Fields Boosted";

    // =================================
    // Misc
    // =================================
    private static final Random random = new Random();

    /**
     * Serves as the launcher of current class (application).
     * 
     * @param args
     *            The launch arguments.
     * @throws InterruptedException
     *             The InterruptedException exception.
     */
    public static void main(String[] args) throws InterruptedException {
        BookManager bookManager = new BookManager();

        bookManager.startIndexingBooks();

        bookManager.deleteAllBooks();
        bookManager.saveSomeBooks();
        bookManager.listAllBooks();

        // =================================================
        // Search as Different Queries
        // =================================================
        bookManager.searchAsKeywordQuery();
        bookManager.searchAsFuzzyQuery();
        bookManager.searchAsWildcardQuery();
        bookManager.searchAsPhraseQuery();
        bookManager.searchAsRangeQuery();

        // =================================================
        // Search with Pagination and Sorting
        // =================================================
        bookManager.searchWithPagination();
        bookManager.searchWithSorting();
        bookManager.searchWithPaginationAndSorting();

        // =================================================
        // Search Using Query Features
        // =================================================
        bookManager.searchOnMultipleFields();
        bookManager.searchUsingCombinedQueries();
        bookManager.searchOnChangedFields1();
        bookManager.searchOnChangedFields2();

        // =================================================
        // Search Using Analyzers
        // =================================================
        bookManager.searchUsingAnalyzers();

        // =================================================
        // Search Using Query Options
        // =================================================
        bookManager.searchWithBoostedFactors();
        bookManager.searchWithConstantScores();

        // TODO Add examples about using the following query options.
        // - filteredBy()
        // - ignoreAnalyzer()
        // - ignoreFieldBridge()
    }

    private void startIndexingBooks() throws InterruptedException {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.createIndexer(Book.class).startAndWait();
    }

    /**
     * Delete all books.
     */
    public void deleteAllBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<Book> results = session.createQuery(QUERY_STATEMENT_FROM_BOOK).list();

        for (Book result : results) {
            session.delete(result);
        }

        session.getTransaction().commit();
        session.close();
    }

    /**
     * Save some books.
     */
    public void saveSomeBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        for (int i = 0; i < 3; i++) {
            session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                    generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                    generateBookPublisher(session)));
        }

        // ---------------------------------------------------------------------

        session.save(new Book(BOOK_ISBN_01, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_02, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(BOOK_ISBN_03, generateBookName(), generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

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

        session.save(new Book(generateBookIsbn(), BOOK_NAME_61, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), BOOK_NAME_61, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_AUTHOR_NAME_01, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_01,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_02,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_02,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_03,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_03,
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

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_21,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), BOOK_PRICE_21,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_01, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_02, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_INTRO_03, generateBookPublicationDate(), generateBookAwarded(), generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        String[] bookRemarks = { BOOK_REMARKS_01, BOOK_REMARKS_02, BOOK_REMARKS_03, BOOK_REMARKS_04, BOOK_REMARKS_05,
                BOOK_REMARKS_06, BOOK_REMARKS_07, BOOK_REMARKS_08, BOOK_REMARKS_09 };
        for (int i = 0; i < bookRemarks.length; i++) {
            Book book = new Book(generateBookIsbn(), generateBookName(), BOOK_AUTHOR_NAME_02, generateBookPrice(),
                    generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                    generateBookPublisher(session));
            book.setRemarks(bookRemarks[i]);
            session.save(book);
        }

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), BOOK_COMMON_VALUE_01, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_COMMON_VALUE_01, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_COMMON_VALUE_01, generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), BOOK_COMMON_VALUE_11, generateBookAuthorName(), generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_COMMON_VALUE_11, generateBookPrice(),
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.save(new Book(generateBookIsbn(), generateBookName(), generateBookAuthorName(), generateBookPrice(),
                BOOK_COMMON_VALUE_11, generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        // ---------------------------------------------------------------------

        session.save(new Book(generateBookIsbn(), generateBookName(), BOOK_AUTHOR_NAME_01, BOOK_PRICE_11,
                generateBookIntro(), generateBookPublicationDate(), generateBookAwarded(),
                generateBookPublisher(session)));

        session.getTransaction().commit();
        session.close();
    }

    /**
     * List all books.
     */
    public void listAllBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
        List<Book> queryResults = session.createQuery(QUERY_STATEMENT_FROM_BOOK).list();
        System.out.println();
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
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_SEARCHING_MULTIPLE_WORDS_ON_ONE_FIELD);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_SPECIFYING_EDIT_DISTANCE);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_SPECIFYING_PREFIX_LENGTH);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_THE_QUESTION_MARK);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_THE_ASTERISK_MARK);

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
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_SPECIFYING_SLOP);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_THE_FROM_TO_RANGE);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_THE_ABOVE_RANGE);

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_THE_BELOW_RANGE);

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
    private void searchWithPagination() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        CommonsUtil.printDelimiterLine(true,
                delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

        org.apache.lucene.search.Query luceneQuery = queryBuilder.all().createQuery();
        FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
        CommonsUtil.showQueryString(query);

        int paginationPageSize = 15;
        int paginationPageNo = 2;
        int firstResultIndex = (paginationPageNo - 1) * paginationPageSize;
        query.setMaxResults(paginationPageSize);
        query.setFirstResult(firstResultIndex);

        List<Book> queryResults = query.list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        int resultSize = query.getResultSize();

        System.out.println("paginationPageSize: " + paginationPageSize);
        System.out.println("paginationPageNo: " + paginationPageNo);
        System.out.println("firstResultIndex: " + firstResultIndex);
        System.out.println("resultSize: " + resultSize);

        CommonsUtil.printDelimiterLine();

        fullTextSession.getTransaction().commit();
    }

    /**
     * <pre>
     * - The fields to sort should be annotated using the @SortableField annotation.
     * - Multiple fields can be used together for sorting in one query.
     * </pre>
     */
    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchWithSorting() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        CommonsUtil.printDelimiterLine(true,
                delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

        org.apache.lucene.search.Query luceneQuery = queryBuilder.range().onField(FIELD_NAME_BOOK_PRICE).above(50.0D)
                .createQuery();
        FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
        CommonsUtil.showQueryString(query);

        String sortFieldName = FIELD_NAME_BOOK_NAME;
        Type sortFieldType = SortField.Type.STRING;
        boolean sortOrderDescending = false;
        query.setSort(new Sort(new SortField(sortFieldName, sortFieldType, sortOrderDescending)));

        List<Book> queryResults = query.list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        CommonsUtil.printDelimiterLine();

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchWithPaginationAndSorting() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        CommonsUtil.printDelimiterLine(true,
                delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

        org.apache.lucene.search.Query luceneQuery = queryBuilder.all().createQuery();
        FullTextQuery query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
        CommonsUtil.showQueryString(query);

        String sortFieldName1 = FIELD_NAME_BOOK_PRICE;
        Type sortFieldType1 = SortField.Type.DOUBLE;
        boolean sortOrderDescending1 = true;

        String sortFieldName2 = FIELD_NAME_BOOK_NAME;
        Type sortFieldType2 = SortField.Type.STRING;
        boolean sortOrderDescending2 = false;

        query.setSort(new Sort(new SortField(sortFieldName1, sortFieldType1, sortOrderDescending1),
                new SortField(sortFieldName2, sortFieldType2, sortOrderDescending2)));

        int paginationPageSize = 15;
        int paginationPageNo = 1;
        int firstResultIndex = (paginationPageNo - 1) * paginationPageSize;
        query.setMaxResults(paginationPageSize);
        query.setFirstResult(firstResultIndex);

        List<Book> queryResults = query.list();
        for (Book queryResult : queryResults) {
            System.out.println(queryResult);
        }

        int resultSize = query.getResultSize();

        System.out.println("paginationPageSize: " + paginationPageSize);
        System.out.println("paginationPageNo: " + paginationPageNo);
        System.out.println("firstResultIndex: " + firstResultIndex);
        System.out.println("resultSize: " + resultSize);

        CommonsUtil.printDelimiterLine();

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
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_USING_ONE_ONFIELDS_METHOD);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword()
                    .onFields(FIELD_NAME_BOOK_NAME, FIELD_NAME_BOOK_AUTHOR_NAME, FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE_01).createQuery();
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
                            + MESSAGE_TEXT_USING_ONE_ONFIELD_METHOD_AND_MULTIPLE_ANDFIELD_METHODS);

            // Here, the 2nd field is treated differently by performing an
            // additional setting.
            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .andField(FIELD_NAME_BOOK_AUTHOR_NAME).boostedTo(3.0F).andField(FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE_01).createQuery();
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
                            + MESSAGE_TEXT_USING_THE_MUST_METHOD_FOR_AN_AND_QUERY);

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
                            + MESSAGE_TEXT_USING_THE_SHOULD_METHOD_FOR_AN_OR_QUERY);

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
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_USING_THE_NOT_METHOD_FOR_A_NOT_QUERY);

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
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_USING_THE_ALL_METHOD_FOR_AN_ALL_QUERY);

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
                            + MESSAGE_TEXT_USING_THE_EXCEPT_METHOD_TO_EXCLUDE_RESULTS);

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
     * 
     * If the values of the field "intro" contain statements in following format,
     *     <code>key="KEY" value="VALUE"</code>
     * additional fields will be added into the documents.
     * 
     * For entities which intro is BOOK_INTRO_01, BOOK_INTRO_02 or BOOK_INTRO_03 defined above,
     * their documents after indexing will have additional fields as follows.
     *          Value
     * Name     Document #1         Document #2         Document#3
     * -------------------------------------------------------------------------
     * itemA    valueA1 valueA2     valueA1             ----
     * itemB    valueB1             valueB2             valueB3
     * itemC    ----                valueC1             valueC1 valueC3
     * </pre>
     */
    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchOnChangedFields1() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter));

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_A).sentence(VALUE_A1)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_A).sentence(VALUE_A2)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_A).sentence(VALUE_A3)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_B).sentence(VALUE_B1)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_B).sentence(VALUE_B2)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_B).sentence(VALUE_B3)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_C).sentence(VALUE_C1)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_C).sentence(VALUE_C2)
                    .createQuery();
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

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_C).sentence(VALUE_C3)
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

    /**
     * <pre>
     * Because values of the field "awarded" have been changed in the field's bridge implementation per the following rule,
     *     Before Change (Boolean)      After Change (String)
     *     ---------------------------------------------------------------------
     *     true                         "1"
     *     false                        "0"
     *     <All Other Cases>            "0"
     * the values "0" and "1" instead of "false" and "true" should be used in the search.
     * </pre>
     */
    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchOnChangedFields2() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_SEARCHING_ALL_BOOKS_AWARDED);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.phrase().onField(FIELD_NAME_BOOK_AWARDED)
                    .sentence(String.valueOf(NumberUtils.INTEGER_ONE)).createQuery();
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
    private void searchUsingAnalyzers() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // No Testing
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_NO_TESTING);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Testing the Standard Tokenizer - 1
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_TESTING_THE_STANDARD_TOKENIZER_1);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching("~b02`c03!").createQuery();
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
        // Testing the Standard Tokenizer - 2
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_TESTING_THE_STANDARD_TOKENIZER_2);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching(BOOK_REMARKS_01).createQuery();
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
        // Testing the Lower Case Filter
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_TESTING_THE_LOWER_CASE_FILTER);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching("B02").createQuery();
            org.apache.lucene.search.Query luceneSubquery3 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching("a27").createQuery();
            org.apache.lucene.search.Query luceneQuery = queryBuilder.bool().must(luceneSubquery1).must(luceneSubquery2)
                    .must(luceneSubquery3).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // TODO Investigate why searching by "do" using the the Snowball Porter
        // filter does not return results containing "did" and "does".
        // =====================================================================
        // Testing the Snowball Porter Filter - 1
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_TESTING_THE_SNOWBALL_PORTER_FILTER_1);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching("do").createQuery();
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
        // Testing the Snowball Porter Filter - 2
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true,
                    delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + (++testCounter)
                            + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                            + MESSAGE_TEXT_TESTING_THE_SNOWBALL_PORTER_FILTER_2);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_AUTHOR_NAME)
                    .matching(BOOK_AUTHOR_NAME_02).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_REMARKS)
                    .matching("refactor").createQuery();
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

        fullTextSession.getTransaction().commit();
    }

    @SuppressWarnings(CommonsUtil.COMPILER_WARNING_NAME_UNCHECKED)
    private void searchWithBoostedFactors() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Queries Not Boosted
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_QUERIES_NOT_BOOSTED);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .matching(BOOK_NAME_61).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_PRICE)
                    .matching(BOOK_PRICE_21).createQuery();
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

        // TODO It seems the boost to the queries does not work.
        // =====================================================================
        // Queries Boosted
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_QUERIES_BOOSTED);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .matching(BOOK_NAME_61).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().boostedTo(5.0F)
                    .onField(FIELD_NAME_BOOK_PRICE).matching(BOOK_PRICE_21).createQuery();
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
        // Fields Not Boosted
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_FIELDS_NOT_BOOSTED);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .andField(FIELD_NAME_BOOK_AUTHOR_NAME).andField(FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE_11).createQuery();
            Query query = fullTextSession.createFullTextQuery(luceneQuery, Book.class);
            CommonsUtil.showQueryString(query);

            List<Book> queryResults = query.list();
            for (Book queryResult : queryResults) {
                System.out.println(queryResult);
            }

            CommonsUtil.printDelimiterLine();
        }

        // =====================================================================
        // Fields Boosted
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_FIELDS_BOOSTED);

            org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .andField(FIELD_NAME_BOOK_AUTHOR_NAME).boostedTo(5.0F).andField(FIELD_NAME_BOOK_INTRO)
                    .matching(BOOK_COMMON_VALUE_11).createQuery();
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
    private void searchWithConstantScores() {
        FullTextSession fullTextSession = Search.getFullTextSession(HibernateUtil.getSessionFactory().openSession());
        fullTextSession.beginTransaction();

        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();

        String delimiterLinePrefixBase = new Object() {
        }.getClass().getEnclosingMethod().getName();
        int testCounter = 0;

        // =====================================================================
        // Constant Score Not Set
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_QUERIES_NOT_BOOSTED);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .matching(BOOK_NAME_61).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_PRICE)
                    .matching(BOOK_PRICE_21).createQuery();
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

        // TODO Unsure whether the following result is correct or not.
        // =====================================================================
        // Constant Score Set
        // =====================================================================
        {
            CommonsUtil.printDelimiterLine(true, delimiterLinePrefixBase + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR
                    + (++testCounter) + CommonsUtil.DELIMITER_LINE_PREFIX_CONNECTOR + MESSAGE_TEXT_QUERIES_BOOSTED);

            org.apache.lucene.search.Query luceneSubquery1 = queryBuilder.keyword().onField(FIELD_NAME_BOOK_NAME)
                    .matching(BOOK_NAME_61).createQuery();
            org.apache.lucene.search.Query luceneSubquery2 = queryBuilder.keyword().boostedTo(5.0F).withConstantScore()
                    .onField(FIELD_NAME_BOOK_PRICE).matching(BOOK_PRICE_21).createQuery();
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

        fullTextSession.getTransaction().commit();
    }

    private static final String generateBookIsbn() {
        return RandomStringUtils.randomNumeric(13);
    }

    private static final String generateBookName() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private static final String generateBookAuthorName() {
        return RandomStringUtils.randomAlphanumeric(12);
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

        // 1985-01-01 00:00:00:000
        calendar.set(Calendar.YEAR, 1985);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long minTimeValue = calendar.getTimeInMillis();

        // 2015-12-31 23:59:59:999
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Long maxTimeValue = calendar.getTimeInMillis();

        return new Date(RandomUtils.nextLong(minTimeValue, maxTimeValue));
    }

    private static final boolean generateBookAwarded() {
        return random.nextBoolean();
    }

    private static final Publisher generateBookPublisher(Session session) {
        return session.get(Publisher.class, session.save(
                new Publisher(RandomStringUtils.randomAlphanumeric(15), RandomStringUtils.randomAlphanumeric(30))));
    }

}