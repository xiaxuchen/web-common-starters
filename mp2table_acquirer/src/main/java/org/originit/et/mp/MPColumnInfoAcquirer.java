package org.originit.et.mp;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import org.originit.et.converter.TypeConverter;
import org.originit.et.info.AbstractColumnInfoAcquirer;
import org.originit.et.info.ColumnInfoAcquirer;
import org.originit.et.info.impl.JPAColumnInfoAcquirer;
import org.originit.et.jdbc.JDBCType;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xxc
 */
public class MPColumnInfoAcquirer extends AbstractColumnInfoAcquirer {

    private Map<Class<?>,TypeConverter> convertRules;

    public MPColumnInfoAcquirer(Map<Class<?>, TypeConverter> convertRules) {
        super(convertRules);
    }

    @Override
    public boolean isId(Field field) {
        return AnnotationUtil.hasAnnotation(field, TableId.class);
    }

    @Override
    public boolean isIdAuto(Field field) {
        final TableId generatedValue = AnnotationUtil.getAnnotation(field, TableId.class);
        if (generatedValue != null) {
            return generatedValue.value().equals(IdType.AUTO);
        }
        return false;
    }
    @Override
    public String columnName(Field field) {
        final TableField annotation = AnnotationUtil.getAnnotation(field, TableField.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            return field.getName();
        }
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
        final ApiModelProperty annotation = AnnotationUtil.getAnnotation(declaredField, ApiModelProperty.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

}
