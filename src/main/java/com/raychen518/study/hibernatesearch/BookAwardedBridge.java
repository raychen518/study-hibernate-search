package com.raychen518.study.hibernatesearch;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.bridge.StringBridge;

/**
 * <pre>
 * This field bridge implementation changes the field values per the following rule.
 * 
 * Before Change (Boolean)      After Change (String)
 * ---------------------------------------------------------------------
 * true                         "1"
 * false                        "0"
 * <All Other Cases>            "0"
 * </pre>
 */
public class BookAwardedBridge implements StringBridge {

    @Override
    public String objectToString(Object object) {
        Integer result = NumberUtils.INTEGER_ZERO;

        if (object instanceof Boolean) {
            result = (Boolean) object ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
        }

        return String.valueOf(result);
    }

}
