package org.originit.crud.provider.impl

import org.apache.ibatis.jdbc.SQL
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.provider.BaseSQLProvider

class SelectByIdSQLProvider: BaseSQLProvider() {
    override fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String {
        // 我想要mapperType上的泛型
        val tableName = metaInfo.tableName
        val sql = SQL()
        sql.SELECT("*").FROM(tableName).WHERE("${metaInfo.idColumn}=#{${metaInfo.idField}}")
        if (metaInfo.logicField != null) {
            sql.WHERE("${metaInfo.logicColumn}=1")
        }
        return sql.toString()
    }
}