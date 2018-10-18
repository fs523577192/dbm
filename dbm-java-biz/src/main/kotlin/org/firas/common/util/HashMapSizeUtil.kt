package org.firas.common.util

import kotlin.math.ceil

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月10日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
fun hashMapSizeFor(size: Int): Int {
    if (size < 0) {
        throw IllegalArgumentException("Negative size: %d".format(size))
    }
    try {
        val defaultLoadFactorField = HashMap::class.java.getDeclaredField(
                "DEFAULT_LOAD_FACTOR")
        defaultLoadFactorField.setAccessible(true)
        val defaultLoadFactor = defaultLoadFactorField.get(null) as Float
        return ceil(size / defaultLoadFactor).toInt()
    } catch (ex: Exception) {
        return ceil(size / 0.75).toInt()
    }
}

fun hashMapSizeFor(size: Int, hashMap: HashMap<*, *>): Int {
    if (size < 0) {
        throw IllegalArgumentException("Negative size: %d".format(size))
    }
    try {
        val loadFactorField = HashMap::class.java.getDeclaredField(
                "loadFactor")
        loadFactorField.setAccessible(true)
        val loadFactor = loadFactorField.get(hashMap) as Float
        return ceil(size / loadFactor).toInt()
    } catch (ex: Exception) {
        return hashMapSizeFor(size)
    }
}

fun hashSetSizeFor(size: Int, hashSet: HashSet<*>): Int {
    if (size < 0) {
        throw IllegalArgumentException("Negative size: %d".format(size))
    }
    try {
        val mapField = HashSet::class.java.getDeclaredField(
                "map")
        return hashMapSizeFor(size, mapField.get(hashSet) as HashMap<*, *>)
    } catch (ex: Exception) {
        return ceil(size / 0.75).toInt()
    }
}
