package org.firas.dbm.type

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
actual fun toDbType(str: String): DbType {
    val i = str.indexOf('{')
    if (i <= 0) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }
    val typeName = str.substring(0, i)
    if ("DbType".equals(typeName)) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }

    return jacksonObjectMapper().readValue(str.substring(i),
            java.lang.Class.forName("org.firas.dbm.type.${typeName}"))
            as DbType
}