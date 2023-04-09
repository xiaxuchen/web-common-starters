package org.originit.et.converter.impl;

import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.jdbc.JDBCType;

public class DefaultIntegerTypeConverter implements DefaultTypeConverter {
    @Override
    public Class<?> javaType() {
        return Integer.class;
    }

    @Override
    public JDBCType jdbcType() {
        return JDBCType.INT;
    }
}
