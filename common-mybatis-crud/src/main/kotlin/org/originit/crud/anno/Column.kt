package org.originit.crud.anno

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
/**
 * value 数据库列名
 */
annotation class Column(val value: String, val updateIfNull: Boolean = false)
