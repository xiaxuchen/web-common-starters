package org.originit.crud.util

import java.util.*

fun String.camelCaseToUnderscores(): String {
    val regex = Regex("([a-z])([A-Z])")
    return replace(regex, "$1_$2").lowercase(Locale.getDefault())
}