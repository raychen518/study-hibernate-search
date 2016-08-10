package com.raychen518.study.hibernatesearch;

import org.hibernate.search.bridge.StringBridge;

public class BookIntroBridge implements StringBridge {

    @Override
    public String objectToString(Object object) {
        return (object == null) ? null : object.toString().trim().replaceAll("\\d", "");
    }

}
