package org.originit.crud.anno

import org.originit.crud.enums.KeyGeneratorPolicy

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class KeyGenerator (val value:KeyGeneratorPolicy = KeyGeneratorPolicy.AUTO)