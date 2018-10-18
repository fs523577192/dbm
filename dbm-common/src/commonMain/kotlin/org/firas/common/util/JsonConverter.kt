package org.firas.common.util

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
expect class JsonConverter {

    fun stringify(o: Any?): String

    fun <T> parse(text: String): T
}

expect fun getJsonConverter(): JsonConverter