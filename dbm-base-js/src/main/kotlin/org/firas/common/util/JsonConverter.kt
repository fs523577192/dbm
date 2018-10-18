package org.firas.common.util

import kotlin.js.JSON

actual class JsonConverter {

    companion object {
        val instance = JsonConverter()
    }

    actual fun stringify(o: Any?): String {
        return JSON.stringify(o)
    }

    actual fun <T> parse(text: String): T {
        return JSON.parse<T>(text)
    }
}

actual fun getJsonConverter(): JsonConverter {
    return JsonConverter.instance
}