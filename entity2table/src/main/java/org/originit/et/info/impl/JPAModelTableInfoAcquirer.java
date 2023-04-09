package org.originit.et.info.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import org.originit.et.anno.Comment;
import org.originit.et.info.AbstractModelTableInfoAcquirer;
import org.originit.et.info.ColumnInfoAcquirer;

import javax.persistence.Table;

/**
 * @author xxc
 */
public class JPAModelTableInfoAcquirer extends AbstractModelTableInfoAcquirer {

    public JPAModelTableInfoAcquirer(ColumnInfoAcquirer columnInfoAcquirer) {
        super(columnInfoAcquirer);
    }

    @Override
    public void isTable(Class<?> c) {
        final Table table = AnnotationUtil.getAnnotation(c, Table.class);
        if (table == null) {
            throw new IllegalArgumentException(c.getName() + " must annotated with javax.persistence.Table to specify table name!");
        }
        if (table.name() == null || StrUtil.isBlank(table.name())) {
            throw new IllegalArgumentException(" table name can't be empty or null," + c.getName() + " must annotated with javax.persistence.Table to specify table name!");
        }
    }

    @Override
    public String getTableName(Class<?> c) {
        final Table annotation = AnnotationUtil.getAnnotation(c, Table.class);
        return annotation.name();
    }

    @Override
    public String getComment(Class<?> c) {
        final Comment annotation = c.getAnnotation(Comment.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }
}
