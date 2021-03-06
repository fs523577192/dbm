package org.firas.dbm.type

import java.math.BigDecimal
import kotlin.reflect.KClass

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
actual class DecimalType(val precision: Int, val scale: Int): DbType() {

    init {
        if (precision <= 0) {
            throw IllegalArgumentException("Decimal的precision必须是一个正整数：${precision}")
        }
        if (scale < 0) {
            throw IllegalArgumentException("Decimal的scale必须是一个非负整数：${scale}")
        }
    }

    override fun toKotlinType(): KClass<*> {
        return BigDecimal::class
    }
}