package org.firas.dbm.dialect

import org.firas.dbm.type.DbType

/**
 * 数据库方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface DbDialect {

    fun getJdbcType(dbType: DbType): String

    /**
     * @return 用来检查数据库连接是否有效的查询SQL
     */
    fun validateQuery(): String

    fun getNameQuote(): String

    fun getCharset(): DbCharset

    fun toSQL(columnComment: ColumnComment): String
}
