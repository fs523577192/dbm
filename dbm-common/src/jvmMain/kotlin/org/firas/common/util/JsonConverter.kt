package org.firas.common.util

import java.lang.ThreadLocal
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月17日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
actual class JsonConverter {

    companion object {
        val instance = JsonConverter()
        private val cache = ThreadLocal.withInitial<ObjectMapper>({ jacksonObjectMapper() })
    }

    actual fun stringify(o: Any?): String {
        return cache.get().writeValueAsString(o)
    }

    actual fun <T> parse(text: String): T {
        return cache.get().readValue(text, object: TypeReference<T>() {})
    }
}

actual fun getJsonConverter(): JsonConverter {
    return JsonConverter.instance
}
