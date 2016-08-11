package com.raychen518.study.hibernatesearch;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

public class BookAwardedBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        luceneOptions.addFieldToDocument(name, String.valueOf(value), document);
    }

}
