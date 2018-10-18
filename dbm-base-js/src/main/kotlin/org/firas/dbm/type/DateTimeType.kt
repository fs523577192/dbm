package org.firas.dbm.type

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月18日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
actual class DateTimeType(val fractional: Int = 0): DbType() {

    override fun toKotlinType(): kotlin.reflect.KClass<*> {
        return kotlin.js.Date::class
    }
}
