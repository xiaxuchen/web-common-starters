package org.originit.et.info;

import cn.hutool.core.util.StrUtil;
import org.originit.et.entity.Column;
import org.originit.et.entity.Id;
import org.originit.et.entity.Table;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xxc
 */
public interface ModelTableInfoAcquirer {

    /**
     * 是否符合表的注释
     * @param c
     * @return
     */
    void isTable(Class<?> c) throws IllegalStateException;

    /**
     * 通过class获取表名
     * @param c
     * @return
     */
    String getTableName(Class<?> c);

    /**
     * 获取表的注释
     * @param c
     * @return
     */
    String getComment(Class<?> c);

    /**
     * 获取表的列
     * @param c
     * @return
     */
    List<Column> getColumns(Class<?> c);

    /**
     * 获取表信息
     * @param c
     * @return
     */
    default Table getTable(Class<?> c) {
        final List<Column> columns = getColumns(c);
        columns.forEach(column -> column.setName(StrUtil.toUnderlineCase(column.getName())));
        final List<Column> idList = columns.stream().filter(column -> column instanceof Id).collect(Collectors.toList());
        if (idList.size() > 1) {
            throw new IllegalArgumentException("暂不支持多列主键,table:" + getTableName(c) + "idFields:" + idList.stream().map(column -> column.getName()).collect(Collectors.toList()));
        }
        Id id = idList.size() == 0?null: (Id) idList.get(0);
        return Table.builder().columns(columns.stream().filter(column -> !(column instanceof Id)).collect(Collectors.toList())).id(id).comment(getComment(c)).name(getTableName(c)).build();
    }
}
