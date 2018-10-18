package org.firas.dbm.type

import org.firas.common.util.getJsonConverter

/**
 * 数据库中的数据类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class DbType {

    abstract fun toKotlinType(): kotlin.reflect.KClass<*>

    override fun toString(): String {
        val className = this::class.simpleName
        val json = getJsonConverter().stringify(this)
        return "${className}${json}"
    }
}

expect fun toDbType(str: String): DbType