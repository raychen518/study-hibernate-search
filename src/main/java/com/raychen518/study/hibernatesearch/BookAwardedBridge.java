package com.raychen518.study.hibernatesearch;

import org.hibernate.search.bridge.StringBridge;

public class BookAwardedBridge implements StringBridge {

    @Override
    public String objectToString(Object object) {
        return (object == null) ? null : String.valueOf(object);
    }

}
