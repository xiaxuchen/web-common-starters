CREATE TABLE IF NOT EXISTS ${model.name}(
<#if model.id??>
    `${model.id.name}` ${model.id.type}<#if model.id.length??>(${model.id.length?c})</#if> <#if model.id.notNull??> NOT NULL </#if> PRIMARY KEY <#if model.id.auto> AUTO_INCREMENT </#if><#if (model.columns?size>0)>,</#if>
</#if>
<#if model.columns??>
    <#list model.columns as column>
    `${column.name}` ${column.type}<#if column.length??>(${column.length?c})</#if><#if column.notNull??> NOT NULL </#if><#if column.comment??> COMMENT '${column.comment}'</#if><#if column_has_next>,</#if>
    </#list>
</#if>
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='${model.comment?default("")}';

