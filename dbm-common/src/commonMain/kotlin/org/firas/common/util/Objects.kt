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
class Objects

fun safeEquals(a: Any?, b: Any?): Boolean {
    return if (null == a) null == b else a.equals(b)
}

fun safeHashCode(a: Any?): Int {
    return a?.hashCode() ?: 0
}