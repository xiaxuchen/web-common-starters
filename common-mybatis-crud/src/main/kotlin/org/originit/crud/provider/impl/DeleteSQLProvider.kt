package org.originit.crud.provider.impl

import org.apache.ibatis.jdbc.SQL
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.provider.BaseSQLProvider
import org.originit.crud.util.SQLUtils

class DeleteSQLProvider: BaseSQLProvider() {
    override fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String {
        val sql = SQL()
        if (metaInfo.logicField == null) {
            sql.DELETE_FROM(metaInfo.tableName)
        } else {
            sql.UPDATE(metaInfo.tableName).SET("${metaInfo.logicColumn}=0")
        }
        SQLUtils.conditions(sql, params, metaInfo)
        return sql.toString()
    }
}