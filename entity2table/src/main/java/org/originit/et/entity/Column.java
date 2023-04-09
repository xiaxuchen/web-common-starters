package org.originit.et.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表的列
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Column {

    private String name;

    private String type;

    private Integer length;

    private boolean unique;

    private boolean notNull;

    private String comment;
}
