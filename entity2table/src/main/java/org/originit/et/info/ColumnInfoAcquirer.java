package org.originit.et.info;

import org.originit.et.entity.Column;
import org.originit.et.entity.Id;
import org.originit.et.jdbc.JDBCType;

import java.lang.reflect.Field;

/**
 * @author xxc
 */
public interface ColumnInfoAcquirer {

    /**
     * 是否为表的列
     * @param field
     * @return
     */
    boolean isTableField(Field field);

    /**
     * 是否为id列
     * @param field
     * @return
     */
    boolean isId(Field field);

    /**
     * 是否id自增
     * @param field
     * @return
     */
    boolean isIdAuto(Field field);

    /**
     * 列名
     * @param field
     * @return
     */
    String columnName(Field field);

    /**
     * 列类型
     * @param field
     * @return
     */
    JDBCType jdbcType(Field field);

    /**
     * 是否唯一
     * @param field
     * @return
     */
    boolean unique(Field field);

    /**
     * 是否非空
     */
    boolean notNull(Field field);

    /**
     * 注释信息
     * @param declaredField
     * @return
     */
    String comment(Field declaredField);

    /**
     * 字段长度
     * @param declaredField
     * @return
     */
    Integer length(Field declaredField);

    /**
     * 获取列信息
     * @param field 属性
     * @return
     */
    default Column getColumn(Field field) {
        Column column = null;
        if (this.isId(field)) {
            Id id = new Id();
            id.setAuto(isIdAuto(field));
            column = id;
        } else {
            column = new Column();
        }
        column.setComment(comment(field));
        column.setLength(length(field));
        column.setNotNull(notNull(field));
        column.setName(columnName(field));
        column.setUnique(unique(field));
        column.setType(jdbcType(field).name());
        return column;
    }
}
