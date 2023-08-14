package org.originit.et.info.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import org.originit.et.anno.Comment;
import org.originit.et.converter.TypeConverter;
import org.originit.et.info.AbstractColumnInfoAcquirer;
import org.originit.et.info.ColumnInfoAcquirer;
import org.originit.et.jdbc.JDBCType;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xxc
 */
public class JPAColumnInfoAcquirer extends AbstractColumnInfoAcquirer {

    public JPAColumnInfoAcquirer(Map<Class<?>, TypeConverter> convertRules) {
        super(convertRules);
    }


    @Override
    public boolean isId(Field field) {
        return AnnotationUtil.hasAnnotation(field, Id.class);
    }

    @Override
    public boolean isIdAuto(Field field) {
        final GeneratedValue generatedValue = AnnotationUtil.getAnnotation(field, GeneratedValue.class);
        if (generatedValue != null) {
            return generatedValue.strategy().equals(GenerationType.AUTO);
        }
        return false;
    }

    @Override
    public String columnName(Field field) {
        final Column annotation = AnnotationUtil.getAnnotation(field, Column.class);
        if (annotation != null) {
            return annotation.name();
        } else {
            return field.getName();
        }
    }

    @Override
    public JDBCType jdbcType(Field field) {
        final Class<?> type = field.getType();
        if (!convertRules.containsKey(type)) {
            throw new IllegalStateException("请为类型" + type.getName() + "配置TypeConverter");
        }
        return this.convertRules.get(type).jdbcType();
    }

    @Override
    public String comment(Field declaredField) {
        final Comment annotation = AnnotationUtil.getAnnotation(declaredField,Comment.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

}
