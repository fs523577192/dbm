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
actual fun toDbType(str: String): DbType {
    val i = str.indexOf('{')
    if (i <= 0) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }
    val typeName = str.substring(0, i)
    if ("DbType".equals(typeName)) {
        throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }

    return when (typeName) {
        "IntegerType" -> getJsonConverter().parse<IntegerType>(str.substring(i))
        "BigIntType" -> getJsonConverter().parse<BigIntType>(str.substring(i))
        "SmallIntType" -> getJsonConverter().parse<SmallIntType>(str.substring(i))
        "VarcharType" -> getJsonConverter().parse<VarcharType>(str.substring(i))
        "DecimalType" -> getJsonConverter().parse<DecimalType>(str.substring(i))
        "DateTimeType" -> getJsonConverter().parse<DateTimeType>(str.substring(i))
        "DoubleType" -> getJsonConverter().parse<DoubleType>(str.substring(i))
        "FloatType" -> getJsonConverter().parse<FloatType>(str.substring(i))
        "ClobType" -> getJsonConverter().parse<ClobType>(str.substring(i))
        "BlobType" -> getJsonConverter().parse<BlobType>(str.substring(i))
        else -> throw IllegalArgumentException("该字符串不能被转换为数据库类型的实例")
    }
}