package org.originit.et.converter.impl;

import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.jdbc.JDBCType;

import java.time.LocalDateTime;

public class DefaultLocalDateTimeTypeConverter implements DefaultTypeConverter {
    @Override
    public Class<?> javaType() {
        return LocalDateTime.class;
    }

    @Override
    public JDBCType jdbcType() {
        return JDBCType.DATETIME;
    }
}
