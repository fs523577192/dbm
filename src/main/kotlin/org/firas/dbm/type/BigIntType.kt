package org.firas.dbm.type

import kotlin.reflect.KClass

/**
 * 64位整数类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class BigIntType(val unsigned: Boolean = false): DbType {

    override fun getKotlinType(): KClass<*> {
        return Long::class
    }
}