package com.raychen518.study.hibernatesearch;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

/**
 * This class represents a publisher entity.
 */
public class Publisher {

    private Long id;

    @Field(store = Store.YES)
    private String name;

    private String address;

    /**
     * Construct a publisher.
     */
    public Publisher() {
        // This empty default constructor should be kept for internal use from
        // Hibernate Search.
    }

    /**
     * Construct a publisher by the specified information.
     * 
     * @param name
     *            The name of the publisher.
     * @param address
     *            The address of the publisher.
     */
    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Publisher [id=" + id + ", name=" + name + ", address=" + address + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
