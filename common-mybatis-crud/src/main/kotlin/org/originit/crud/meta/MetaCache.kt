package org.originit.crud.meta

import org.originit.crud.enums.KeyGeneratorPolicy
import org.springframework.util.IdGenerator
import javax.crypto.KeyGenerator


class FieldMetaInfo(var fieldName: String? = null,
                    var columnName: String? = null,
                    var primary: Boolean = false,
                    var updateIfNull: Boolean = false) {
}
class ClassMetaInfo(var tableName: String? = null,
                    var entityClass: Class<*>? = null,
                    var idField: String? = null,
                    var idColumn: String? = null,
                    var logicField: String? = null,
                    var logicColumn: String? = null,
                    var idGenerator: KeyGeneratorPolicy = KeyGeneratorPolicy.NONE,
                    var fields: MutableList<FieldMetaInfo> = mutableListOf()
) {
}

object MetaCache {

    private val cache: MutableMap<Class<*>,ClassMetaInfo> = mutableMapOf()

    fun getCache(clazz: Class<*>): ClassMetaInfo? {
        return cache[clazz]
    }

    fun putCache(mapperType: Class<*>, classMetaInfo: ClassMetaInfo) {
        cache[mapperType] = classMetaInfo
    }
}