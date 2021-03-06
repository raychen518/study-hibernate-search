package com.raychen518.study.hibernatesearch;

import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class ProductA extends Product {

    private static final String NAME = "Product A";

    private String featureA1;

    private String featureA2;

    public ProductA() {
    }

    public ProductA(Long serialNumber, String feature, String featureA1, String featureA2, Date createdDate) {
        this.serialNumber = serialNumber;
        this.name = NAME;
        this.feature = feature;
        this.featureA1 = featureA1;
        this.featureA2 = featureA2;
        this.createdDate = createdDate;
    }

    public String getFeatureA1() {
        return featureA1;
    }

    public void setFeatureA1(String featureA1) {
        this.featureA1 = featureA1;
    }

    public String getFeatureA2() {
        return featureA2;
    }

    public void setFeatureA2(String featureA2) {
        this.featureA2 = featureA2;
    }

    @Override
    public String toString() {
        return super.toString() + " ProductA [featureA1=" + featureA1 + ", featureA2=" + featureA2 + "]";
    }

}
