package com.raychen518.study.hibernatesearch;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class Book {

    @Id
    private Long id;

    /**
     * <pre>
     * - The "index" property specifies whether the field value should be indexed.
     * - The "analyze" property specifies whether the field value should be analyzed.
     * - The "store" property specifies whether the field value should be stored.
     * - The "index = Index.YES", "analyze = Analyze.YES" and "store = Store.NO" settings are the default settings for the @Field annotation.
     * </pre>
     */
    @Field
    private String isbn;

    @Field(store = Store.YES)
    private String name;

    @Field
    private String authorName;

    @Field
    private double price;

    /**
     * <pre>
     * A custom field bridge implementation - BookIntroBridge is used here to change the intro to be indexed.
     * </pre>
     */
    @Field(store = Store.YES)
    @FieldBridge(impl = BookIntroBridge.class)
    private String intro;

    /**
     * <pre>
     * A built-in field bridge implementation - DateBridge is used here to define the Date field's resolution to second instead of the default millisecond.
     * 
     * In other words, the millisecond part of the Date field is ignored in the indexing here.
     * 
     * Assuming the number of milliseconds of the Date field is 1259752333267 (2009-12-02 19:12:13.267),
     * then the Date field's value in the indexes will be 1259752333000 (the millisecond part (267) is ignored).
     * </pre>
     */
    @Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
    private Date publicationDate;

    @Field(store = Store.YES)
    @FieldBridge(impl = BookAwardedBridge.class)
    private boolean awarded;

    @IndexedEmbedded
    private Publisher publisher;

    public Book() {
    }

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
                + ", awarded=" + awarded + ", publisher=" + publisher + "]";
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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

}
