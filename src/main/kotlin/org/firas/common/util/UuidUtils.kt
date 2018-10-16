package org.firas.common.util

import java.util.*

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月16日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
fun getUuidAsHexString(): String {
    val uuid = UUID.randomUUID()
    return "%016x%016x".format(
            uuid.mostSignificantBits, uuid.leastSignificantBits)
}