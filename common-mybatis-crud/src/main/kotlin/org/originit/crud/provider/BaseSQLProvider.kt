package org.originit.crud.provider

import org.apache.ibatis.builder.annotation.ProviderContext
import org.apache.ibatis.reflection.SystemMetaObject
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.meta.MetaCache

abstract class BaseSQLProvider {


    fun invoke(params: Any?, providerContext: ProviderContext): String {
        val paramsMap: MutableMap<String, Any> = if (params != null && params is MutableMap<*, *>) {
            params as MutableMap<String, Any>
        } else {
            val m = mutableMapOf<String, Any>()
            if (params != null) {
                for (declaredField in params::class.java.declaredFields) {
                    val metaObject = SystemMetaObject.forObject(params)
                    if (metaObject.getValue(declaredField.name) != null)
                        m[declaredField.name] =  metaObject.getValue(declaredField.name)
                }
            }
            m
        }
        var classMetaInfo: ClassMetaInfo = MetaCache.getCache(providerContext.mapperType)
            ?: throw IllegalStateException("Mapper类未被解析:${providerContext.mapperType.name}")
        val sql = buildSql(paramsMap, classMetaInfo)
        return sql
    }


    abstract fun buildSql(params: MutableMap<String, Any>, metaInfo: ClassMetaInfo): String
}