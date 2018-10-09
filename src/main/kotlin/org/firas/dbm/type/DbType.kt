package org.firas.dbm.type

import com.fasterxml.jackson.databind.ObjectMapper

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

    abstract fun getKotlinType(): kotlin.reflect.KClass<*>

    override fun toString(): String {
        val fullClassName = this.javaClass.name
        val className = fullClassName.substring(fullClassName.lastIndexOf('.'))
        val objectMapper = ObjectMapper()
        return "%s%s".format(className, objectMapper.writeValueAsString(this))
    }
}

fun toDbType(str: String): DbType {
    val splitted = str.split(',', limit = 2)
    if (splitted.size != 2) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }
    if ("DbType".equals(splitted[0])) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }

    val objectMapper = ObjectMapper()
    return objectMapper.readValue(splitted[1],
            Class.forName("og.firas.dbm.type.%s".format(splitted[0])))
            as DbType
}