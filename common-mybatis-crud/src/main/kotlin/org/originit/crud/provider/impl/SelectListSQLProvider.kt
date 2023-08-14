package org.originit.crud.provider.impl

import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.jdbc.SQL
import org.apache.ibatis.jdbc.SqlBuilder
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.provider.BaseSQLProvider
import org.originit.crud.util.SQLUtils

class SelectListSQLProvider: BaseSQLProvider() {
    override fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String {
        val sql = SQL()
        sql.SELECT("*").FROM(metaInfo.tableName)
        SQLUtils.conditions(sql, params, metaInfo)
        if (params.containsKey(metaInfo.idField)) {
            sql.WHERE("${metaInfo.idColumn}=#{${metaInfo.idField}}")
        }
        if (metaInfo.logicField != null) {
            sql.WHERE("${metaInfo.logicColumn}=1")
        }
        return sql.toString()
    }
}