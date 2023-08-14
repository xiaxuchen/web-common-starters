package org.originit.crud.mapper

import org.apache.ibatis.annotations.DeleteProvider
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.Update
import org.apache.ibatis.annotations.UpdateProvider
import org.originit.crud.provider.impl.*
/**
 * 如何去代理掉这个呢？
 */
interface CrudMapper<Type,ID> {

    @InsertProvider(type = InsertSQLProvider::class, method = "invoke")
    fun insert(entity: Type): Int

    @UpdateProvider(type = UpdateSQLProvider::class, method = "invoke")
    fun updateById(entity: Type?): Int
    @SelectProvider(type = SelectByIdSQLProvider::class, method = "invoke")
    fun getById(@Param("id") id: ID): Type?

    @SelectProvider(type = SelectListSQLProvider::class, method = "invoke")
    fun list(entity: Type?): List<Type>

    @DeleteProvider(type = DeleteSQLProvider::class, method = "invoke")
    fun delete(entity: Type?): Int
}