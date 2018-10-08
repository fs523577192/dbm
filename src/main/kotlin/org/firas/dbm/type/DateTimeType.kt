package org.firas.dbm.type

import kotlin.reflect.KClass
import java.util.Date

/**
 * 日期时间类型
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class DateTimeType(val fractianal: Int = 0): DbType {

    override fun getKotlinType(): KClass<*> {
        return Date::class
    }
}
