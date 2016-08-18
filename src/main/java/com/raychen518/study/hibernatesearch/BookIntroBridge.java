package com.raychen518.study.hibernatesearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

public class BookIntroBridge implements FieldBridge, StringBridge {

    private static final String PATTERN_STATEMENT = "key=\"(\\S+?)\" value=\"(\\S+?)\"";
    private static final String PATTERN_DIGIT = "\\d";

    /**
     * <pre>
     * For every found statement <code>key="KEY" value="VALUE"</code> in the field's value,
     * a field (index) should be added into the document with its name being KEY, and its value being VALUE.
     * If that field to add is already there, VALUE should be appended to that field's value with a space prefixed.
     * 
     * For example, if the field contains the following statements,
     *     key="itemA" value="valueA1"
     *     key="itemA" value="valueA2"
     *     key="itemB" value="valueB1"
     *     key="itemB" value="valueB3"
     *     key="itemC" value="valueC2"
     * then in the indexing, besides the field itself, another 3 fields will be added into the document, as follows.
     *     Name     Value
     *     -----------------------------
     *     itemA    valueA1 valueA2
     *     itemB    valueB1 valueB3
     *     itemC    valueC2
     * 
     * The statement pattern is as follows.
     *     key="(\S+?)" value="(\S+?)"
     * </pre>
     */
    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        if (value != null) {
            String bookIntro = (String) value;
            Matcher statementMatcher = Pattern.compile(PATTERN_STATEMENT).matcher(bookIntro);
            Map<String, Set<String>> statements = new HashMap<>();

            while (statementMatcher.find()) {
                String statementKey = statementMatcher.group(1);
                String statementValue = statementMatcher.group(2);

                if (!statements.containsKey(statementKey)) {
                    statements.put(statementKey, new HashSet<String>());
                }

                statements.get(statementKey).add(statementValue);
            }

            for (Entry<String, Set<String>> statementEntry : statements.entrySet()) {
                luceneOptions.addFieldToDocument(statementEntry.getKey(),
                        StringUtils.join(statementEntry.getValue().toArray(new String[0]), StringUtils.SPACE),
                        document);
            }
        }

        luceneOptions.addFieldToDocument(name, String.valueOf(value), document);
    }

    // TODO These 2 methods set(...) and objectToString(...) both create
    // indexes. Is there some wrong here?

    @Override
    public String objectToString(Object object) {
        return (object == null) ? null : object.toString().trim().replaceAll(PATTERN_DIGIT, StringUtils.EMPTY);
    }

}
