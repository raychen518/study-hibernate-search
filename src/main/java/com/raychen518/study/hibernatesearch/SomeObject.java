package com.raychen518.study.hibernatesearch;

import javax.persistence.Id;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * This class represents a SomeObject entity. This class and the
 * SomeObjectManager class demonstrate the usages of analyzers.
 */
@Indexed
@Analyzer(impl = StandardAnalyzer.class)
public class SomeObject {

    @Id
    private Long id;

    @Field(store = Store.YES)
    private String field1;

    @Field(store = Store.YES)
    @Analyzer(impl = StandardAnalyzer.class)
    private String field2;

    @Field(store = Store.YES, analyzer = @Analyzer(impl = StandardAnalyzer.class) )
    private String field3;

    /**
     * Construct a SomeObject object.
     */
    public SomeObject() {
        // This empty default constructor should be kept for internal use from
        // Hibernate Search.
    }

    /**
     * Construct a SomeObject object.
     * 
     * @param field1
     *            The field #1.
     * @param field2
     *            The field #2.
     * @param field3
     *            The field #3.
     */
    public SomeObject(String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    @Override
    public String toString() {
        return "SomeObject [id=" + id + ", field1=" + field1 + ", field2=" + field2 + ", field3=" + field3 + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

}
