package org.firas.dbm.type

import kotlin.reflect.KClass

/**
 * 二进制类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class ClobType(): DbType() {

    override fun getKotlinType(): KClass<*> {
        return Int::class
    }
}
