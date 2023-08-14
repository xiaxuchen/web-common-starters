package org.originit.et.converter;

import org.originit.et.jdbc.JDBCType;

/**
 * 类型映射器，将java类型转换为sql类型
 * @author xxc
 */
public interface TypeConverter {

    /**
     * java类型
     * @return
     */
    Class<?> javaType();

    /**
     * 数据库类型
     * @return
     */
    JDBCType jdbcType();

    /**
     * 默认长度
     * @return
     */
    default Integer defaultLength() {
        return null;
    }

}
