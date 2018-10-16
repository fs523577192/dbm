package org.firas.common.util

import org.junit.Assert
import org.junit.Test

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
class UuidUitlsTest {

    @Test
    fun test() {
        val n = 5000
        val regex = Regex("^[0-9a-f]{32}$")
        val uuidSet = HashSet<String>(n)
        for (i in (1 .. n)) {
            val uuid = getUuidAsHexString()
            Assert.assertTrue(regex.matches(uuid))
            Assert.assertFalse(uuidSet.contains(uuid))
            uuidSet.add(uuid)
        }
    }
}