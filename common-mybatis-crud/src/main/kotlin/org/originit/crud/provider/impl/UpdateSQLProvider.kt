package org.originit.crud.provider.impl

import org.apache.ibatis.jdbc.SQL
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.provider.BaseSQLProvider
import org.originit.crud.util.SQLUtils

class UpdateSQLProvider : BaseSQLProvider() {
    override fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String {
        val sql = SQL()

        sql.UPDATE(metaInfo.tableName)
        val sets = metaInfo.fields.filter { it.updateIfNull
                || params.containsKey(it.fieldName) }
            .joinToString(",") {
            "${it.columnName}=#{${it.fieldName}}"
        }
        sql.SET(sets)
        sql.WHERE("${metaInfo.idColumn}=#{${metaInfo.idField}}")
        return sql.toString()
    }

}