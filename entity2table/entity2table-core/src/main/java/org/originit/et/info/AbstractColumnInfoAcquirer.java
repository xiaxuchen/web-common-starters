package org.originit.et.info;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import org.originit.et.anno.Comment;
import org.originit.et.converter.TypeConverter;
import org.originit.et.jdbc.JDBCType;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xxc
 */
public abstract class AbstractColumnInfoAcquirer implements ColumnInfoAcquirer {

    protected Map<Class<?>,TypeConverter> convertRules;

    public AbstractColumnInfoAcquirer(Map<Class<?>, TypeConverter> convertRules) {
        this.convertRules = convertRules;
    }

    @Override
    public boolean isTableField(Field field) {
        return !AnnotationUtil.hasAnnotation(field,Transient.class) && isBeanField(field);
    }



    protected boolean isBeanField(Field field) {
        final String name = field.getName();
        final String capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
        final Class<?> type = field.getType();
        if (type == boolean.class || type == Boolean.class) {
            final Method getMethod = ClassUtil.getPublicMethod(field.getDeclaringClass(), "is" + capitalName);
            final Method setMethod = ClassUtil.getPublicMethod(field.getDeclaringClass(), "set" + capitalName);
            if (getMethod != null && setMethod != null) {
                return true;
            }
        }
        final Method getMethod = ClassUtil.getPublicMethod(field.getDeclaringClass(), "get" + capitalName);
        if (getMethod == null) {
            return false;
        }
        final Method setMethod = ClassUtil.getPublicMethod(field.getDeclaringClass(), "set" + capitalName, field.getType());
        if (setMethod == null) {
            return false;
        }
        return true;
    }


    @Override
    public JDBCType jdbcType(Field field) {
        final Class<?> type = field.getType();
        if (!convertRules.containsKey(type)) {
            throw new IllegalStateException(field.getDeclaringClass().getName() + "." + field.getName() + "没有合适的类型转换器,请为类型" + type.getName() + "配置TypeConverter");
        }
        return this.convertRules.get(type).jdbcType();
    }

    @Override
    public boolean unique(Field field) {
        final Column annotation = AnnotationUtil.getAnnotation(field, Column.class);
        if (annotation == null) {
            return false;
        }
        return annotation.unique();
    }

    @Override
    public boolean notNull(Field field) {
        final Column annotation = AnnotationUtil.getAnnotation(field, Column.class);
        if (annotation == null) {
            return false;
        }
        final boolean nullable = annotation.nullable();
        return !nullable;
    }

    @Override
    public String comment(Field declaredField) {
        final Comment annotation = AnnotationUtil.getAnnotation(declaredField,Comment.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    @Override
    public Integer length(Field declaredField) {
        final Column annotation = AnnotationUtil.getAnnotation(declaredField, Column.class);
        if (annotation != null) {
            return annotation.length();
        }
        final TypeConverter typeConverter = this.convertRules.get(declaredField.getType());
        if (typeConverter != null){
            return typeConverter.defaultLength();
        }
        return null;
    }
}
