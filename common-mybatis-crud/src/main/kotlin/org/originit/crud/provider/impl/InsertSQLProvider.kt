package org.originit.crud.provider.impl

import org.apache.ibatis.jdbc.SQL
import org.originit.crud.enums.KeyGeneratorPolicy
import org.originit.crud.key.UUIDGenerator
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.provider.BaseSQLProvider

class InsertSQLProvider: BaseSQLProvider() {
    override fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String {
        val sql = SQL()
        metaInfo.idGenerator
        var fields = if (metaInfo.idGenerator == KeyGeneratorPolicy.AUTO) metaInfo.fields.filter { it.primary.not() }
        else metaInfo.fields
        fields = fields.filter {
            it.primary || params.containsKey(it.fieldName)
        }
        if (metaInfo.idGenerator === KeyGeneratorPolicy.UUID && metaInfo.idField != null) {
            params[metaInfo.idField!!] = UUIDGenerator.next()
        }
        val columns = fields.filter { params.containsKey(it.fieldName) }
            .map { it.columnName }.joinToString(",")
        val values = fields
            .joinToString(",") { "#{${it.fieldName}}" }
        sql.INSERT_INTO(metaInfo.tableName).VALUES(columns, values)
        return sql.toString()
    }
}