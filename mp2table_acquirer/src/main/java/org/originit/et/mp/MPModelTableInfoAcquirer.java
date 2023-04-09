package org.originit.et.mp;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import org.originit.et.info.AbstractModelTableInfoAcquirer;
import org.originit.et.info.ColumnInfoAcquirer;

/**
 * @author xxc
 */
public class MPModelTableInfoAcquirer extends AbstractModelTableInfoAcquirer {

    public MPModelTableInfoAcquirer(ColumnInfoAcquirer columnInfoAcquirer) {
        super(columnInfoAcquirer);
    }

    @Override
    public void isTable(Class<?> c) {
        final TableName table = AnnotationUtil.getAnnotation(c, TableName.class);
        if (table == null) {
            throw new IllegalArgumentException(c.getName() + " must annotated with javax.persistence.Table to specify table name!");
        }
        if (table.value() == null || StrUtil.isBlank(table.value())) {
            throw new IllegalArgumentException(" table name can't be empty or null," + c.getName() + " must annotated with javax.persistence.Table to specify table name!");
        }
    }

    @Override
    public String getTableName(Class<?> c) {
        final TableName annotation = AnnotationUtil.getAnnotation(c, TableName.class);
        return annotation.value();
    }

    @Override
    public String getComment(Class<?> c) {
        final ApiModel annotation = c.getAnnotation(ApiModel.class);
        if (annotation != null) {
            return annotation.description();
        }
        return null;
    }
}
