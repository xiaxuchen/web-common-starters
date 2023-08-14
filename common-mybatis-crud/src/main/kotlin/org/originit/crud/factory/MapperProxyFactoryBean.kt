package org.originit.crud.factory

import org.mybatis.spring.mapper.MapperFactoryBean
import org.originit.crud.anno.*
import org.originit.crud.mapper.CrudMapper
import org.originit.crud.meta.ClassMetaInfo
import org.originit.crud.meta.FieldMetaInfo
import org.originit.crud.meta.MetaCache
import org.originit.crud.util.camelCaseToUnderscores
import java.lang.IllegalStateException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class MapperProxyFactoryBean<T> : MapperFactoryBean<T> {
    constructor()
    constructor(mapperInterface: Class<T>?) : super(mapperInterface)

    override fun checkDaoConfig() {
        super.checkDaoConfig()
        // 此处要编写解析逻辑
        MetaCache.putCache(this.mapperInterface,resolveMetaInfo(this.mapperInterface))
    }
    private fun resolveMetaInfo(mapperType: Class<*>): ClassMetaInfo {
        val classMetaInfo = ClassMetaInfo()

        val parameterizedType = mapperType.genericInterfaces.filter {type ->
            if (type is ParameterizedType) {
                val parameterizedType = type as ParameterizedType
                val rawType: Type = parameterizedType.rawType
                rawType === CrudMapper::class.java
            } else {
                false
            }
        }[0] as ParameterizedType
        val (entityClass, idType) = parameterizedType.actualTypeArguments
        classMetaInfo.entityClass = entityClass as Class<*>
        classMetaInfo.tableName = if (classMetaInfo.entityClass!!.isAnnotationPresent(TableName::class.java)) {
            classMetaInfo.entityClass!!.getAnnotation(TableName::class.java).value
        } else {
            resolveDefaultTableName(classMetaInfo.entityClass!!)
        }
        for (declaredField in entityClass.declaredFields) {
            if (Modifier.isStatic(declaredField.modifiers) || declaredField.isAnnotationPresent(Transient::class.java)) {
                continue
            }
            val field = FieldMetaInfo()
            if (declaredField.isAnnotationPresent(Id::class.java)) {
                classMetaInfo.idField = declaredField.name
                classMetaInfo.idColumn = declaredField.getAnnotation(Id::class.java).value
                if (classMetaInfo.idColumn == ""){
                    classMetaInfo.idColumn = classMetaInfo.idField
                }
                if (declaredField.isAnnotationPresent(KeyGenerator::class.java)) {
                    classMetaInfo.idGenerator = declaredField.getAnnotation(KeyGenerator::class.java).value
                }
                field.primary = true
            }

            field.fieldName = declaredField.name
            field.updateIfNull = if (declaredField.isAnnotationPresent(Column::class.java)) {
                val column = declaredField.getAnnotation(Column::class.java)
                field.columnName = column.value
                column.updateIfNull
            } else {
                field.columnName = field.fieldName
                false
            }
            if (declaredField.isAnnotationPresent(LogicDelete::class.java)) {
                if (declaredField.type.typeName != "java.lang.Boolean") {
                    throw IllegalStateException("逻辑删除字段必须是Boolean类型，请检查${entityClass.name}.${declaredField.name}")
                }
                if (classMetaInfo.logicField != null) {
                    throw IllegalStateException("存在多个逻辑删除属性:${classMetaInfo.logicField}, ${declaredField.name}")
                }
                classMetaInfo.logicField = field.fieldName
                classMetaInfo.logicColumn = field.columnName
            }

            classMetaInfo.fields.add(field)
        }
        return classMetaInfo
    }

    private fun resolveDefaultTableName(mapperType: Class<*>): String {
        return mapperType.simpleName.camelCaseToUnderscores()
    }

}