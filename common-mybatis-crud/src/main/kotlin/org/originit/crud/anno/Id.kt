package org.originit.crud.anno

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Id(val value: String = "")
