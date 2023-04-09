package org.originit.et.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表格实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Table {

    private String name;

    private List<Column> columns;

    private Id id;

    private String comment;
}
