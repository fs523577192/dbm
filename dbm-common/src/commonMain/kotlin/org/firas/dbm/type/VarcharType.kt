package org.firas.dbm.type

import kotlin.reflect.KClass

/**
 * 变长字符串类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class VarcharType(val length: Int, val charset: String): DbType() {

    init {
        if (length <= 0) {
            throw IllegalArgumentException("Varchar的length必须是一个正整数：${length}")
        }
    }

    override fun toKotlinType(): KClass<*> {
        return String::class
    }
}