package com.raychen518.study.hibernatesearch;

import java.util.Date;

import javax.persistence.Id;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

/**
 * This class represents a book entity. This class and the BookManager class
 * demonstrate most basic usages of Hibernate Search.
 */
@Indexed
@AnalyzerDef(name = "remarksAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class) , filters = {
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                @Parameter(name = "language", value = "English") }) })
public class Book {

    /**
     * <pre>
     * The Above @Indexed Annotation on This Class
     * - To mark the entity to be indexed as the document, using the @Indexed annotation.
     * </pre>
     */

    /**
     * <pre>
     * The Above @AnalyzerDef Annotation on This Class
     * - To define analyzers on the entity, using the @AnalyzerDef / @AnalyzerDefs annotation.
     * 
     * - Here, an analyzer named "remarksAnalyzer" is defined.
     *   It contains the StandardTokenizer tokenizer, the LowerCaseFilter and SnowballPorterFilter filters.
     * </pre>
     */

    /**
     * <pre>
     * To mark the entity field as the document identifier, using the @Id or @DocumentId annotation.
     * </pre>
     */
    @Id
    private Long id;

    /**
     * <pre>
     * - To mark the entity field as a document field, using the @Field annotation.
     * 
     * - About Properties of the @Field Annotation
     *   - The "index" property specifies whether the field value should be indexed.
     *   - The "analyze" property specifies whether the field value should be analyzed.
     *   - The "store" property specifies whether the field value should be stored.
     *   - The "index = Index.YES", "analyze = Analyze.YES" and "store = Store.NO" settings are the default settings.
     * </pre>
     */
    @Field
    private String isbn;

    /**
     * <pre>
     * To mark the document field sortable, using the @SortableField annotation.
     * </pre>
     */
    @Field(store = Store.YES)
    @SortableField
    private String name;

    @Field
    private String authorName;

    @Field
    @SortableField
    private double price;

    /**
     * <pre>
     * - To specify the document field' field bridge implementation, using the @FieldBridge / @DateBridge / etc. annotation.
     * 
     * - Here, a custom field bridge implementation - BookIntroBridge is used to change the "intro" value to be indexed.
     * </pre>
     */
    @Field(store = Store.YES)
    @FieldBridge(impl = BookIntroBridge.class)
    private String intro;

    /**
     * <pre>
     * - By default, the name of the document field is set using the name of the entity field,
     *   but it can be changed by setting the "name" attribute of the @Field annotation.
     * 
     *   Here, the document field's name is set as "publicationTime" instead of the default "publicationDate" from the entity field.
     * 
     * - Here, a built-in field bridge implementation - DateBridge is used
     *   to define this Date field's resolution to second (Resolution.SECOND) instead of the default millisecond (Resolution.MILLISECOND).
     * 
     *   In other words, here, the millisecond part of this Date field is ignored in the indexing.
     * 
     *   To be more specific, assuming the number of milliseconds of this Date field is 1259752333267 (2009-12-02 19:12:13.267) here,
     *   then this Date field's value in the index will be 1259752333000 (the millisecond part (267) is ignored).
     * </pre>
     */
    @Field(name = "publicationTime")
    @DateBridge(resolution = Resolution.SECOND)
    private Date publicationDate;

    /**
     * <pre>
     * Here, a custom field bridge implementation - BookAwardedBridge is used to change the "awarded" value to be indexed.
     * </pre>
     */
    @Field(store = Store.YES)
    @FieldBridge(impl = BookAwardedBridge.class)
    private boolean awarded;

    /**
     * <pre>
     * - To use an analyzer on the document field, using the @Analyzer annotation.
     * 
     * - Here, a custom analyzer named "remarksAnalyzer" is used to analyze the "remarks" value.
     * </pre>
     */
    @Field(store = Store.YES)
    @Analyzer(definition = "remarksAnalyzer")
    private String remarks;

    /**
     * <pre>
     * To mark the entity field as an association, using the @IndexedEmbedded annotation.
     * </pre>
     */
    @IndexedEmbedded
    private Publisher publisher;

    /**
     * Construct a book.
     */
    public Book() {
        // This empty default constructor should be kept for internal use from
        // Hibernate Search.
    }

    /**
     * Construct a book by the specified information.
     * 
     * @param isbn
     *            The book's ISBN.
     * @param name
     *            The book's name.
     * @param authorName
     *            The book's author's name.
     * @param price
     *            The book's price.
     * @param intro
     *            The book's intro.
     * @param publicationDate
     *            The book's publication date.
     * @param awarded
     *            Whether the book has been awarded.
     * @param publisher
     *            The book's publisher.
     */
    public Book(String isbn, String name, String authorName, double price, String intro, Date publicationDate,
            boolean awarded, Publisher publisher) {
        this.isbn = isbn;
        this.name = name;
        this.authorName = authorName;
        this.price = price;
        this.intro = intro;
        this.publicationDate = publicationDate;
        this.awarded = awarded;
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", isbn=" + isbn + ", name=" + name + ", authorName=" + authorName + ", price="
                + price + ", intro=" + CommonsUtil.replaceCrLf(intro) + ", publicationDate=" + publicationDate
                + ", awarded=" + awarded + ", remarks=" + remarks + ", publisher=" + publisher + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isAwarded() {
        return awarded;
    }

    public void setAwarded(boolean awarded) {
        this.awarded = awarded;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

}
