package org.originit.et.converter.impl;

import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.jdbc.JDBCType;

public class DefaultLongTypeConverter implements DefaultTypeConverter {
    @Override
    public Class<?> javaType() {
        return Long.class;
    }

    @Override
    public JDBCType jdbcType() {
        return JDBCType.BIGINT;
    }
}
