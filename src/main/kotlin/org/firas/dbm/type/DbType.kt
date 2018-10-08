package org.firas.dbm.type

/**
 * 数据库中的数据类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface DbType {

    fun getKotlinType(): kotlin.reflect.KClass<*>
}