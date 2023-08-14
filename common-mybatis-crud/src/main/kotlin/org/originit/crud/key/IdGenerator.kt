package org.originit.crud.key

import java.util.UUID

interface IdGenerator {

    fun next():String
}

object UUIDGenerator:IdGenerator {
    override fun next(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}