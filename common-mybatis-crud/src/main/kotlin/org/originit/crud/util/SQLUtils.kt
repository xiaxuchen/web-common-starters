package org.originit.crud.util

import org.apache.ibatis.jdbc.SQL
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.meta.FieldMetaInfo

class SQLUtils {

    companion object {

        fun conditions(sql: SQL, params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): SQL {
            val conditions = metaInfo.fields.filter { params.containsKey(it.fieldName) }.map {
                "${it.columnName}=#{${it.fieldName}}"
            }

            for ((_, condition) in conditions.withIndex()) {
                sql.WHERE(condition)
            }
            return sql
        }
    }
}