package org.originit.et.converter.impl;

import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.jdbc.JDBCType;

/**
 * @author xxc
 */
public class DefaultStringTypeConverter implements DefaultTypeConverter {
    @Override
    public Class<?> javaType() {
        return String.class;
    }

    @Override
    public JDBCType jdbcType() {
        return JDBCType.VARCHAR;
    }

    @Override
    public Integer defaultLength() {
        return 1000;
    }
}
