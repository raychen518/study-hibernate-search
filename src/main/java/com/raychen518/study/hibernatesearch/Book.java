package com.raychen518.study.hibernatesearch;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class Book {

    @Id
    private Long id;

    private Long isbn;

    private int version;

    @Field
    private String content;

    public Book() {
    }

    public Book(Long isbn, int version, String content) {
        this.isbn = isbn;
        this.version = version;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", isbn=" + isbn + ", version=" + version + ", content=" + content + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
