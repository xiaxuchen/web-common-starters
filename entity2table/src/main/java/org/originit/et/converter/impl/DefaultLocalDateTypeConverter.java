package org.originit.et.converter.impl;

import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.jdbc.JDBCType;

import java.time.LocalDate;

public class DefaultLocalDateTypeConverter implements DefaultTypeConverter {
    @Override
    public Class<?> javaType() {
        return LocalDate.class;
    }

    @Override
    public JDBCType jdbcType() {
        return JDBCType.DATE;
    }
}
