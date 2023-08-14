package org.originit.et.info;

import cn.hutool.core.util.ClassUtil;
import org.originit.et.entity.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xxc
 */
public abstract class AbstractModelTableInfoAcquirer implements ModelTableInfoAcquirer{

    protected final ColumnInfoAcquirer columnInfoAcquirer;

    public AbstractModelTableInfoAcquirer(ColumnInfoAcquirer columnInfoAcquirer) {
        this.columnInfoAcquirer = columnInfoAcquirer;
    }

    @Override
    public List<Column> getColumns(Class<?> c) {
        List<Column> columns = new ArrayList<>();
        final Field[] declaredFields = ClassUtil.getDeclaredFields(c);
        for (Field declaredField : declaredFields) {
            if (columnInfoAcquirer.isTableField(declaredField)) {
                columns.add(columnInfoAcquirer.getColumn(declaredField));
            }
        }
        return columns;
    }
}
