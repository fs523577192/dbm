package org.firas.dbm.dialect

import org.firas.dbm.type.DbType

/**
 * MySQL方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class MySQLDialect private constructor(): DbDialect {

    companion object {
        val instance = MySQLDialect()
    }

    override fun getJdbcType(dbType: DbType): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateQuery(): String {
        return "SELECT 1"
    }

    override fun getNameQuote(): String {
        return "`"
    }

    override fun getCharset(): DbCharset {
        return charset
    }

    private val charset: DbCharset = MySQLCharset()

    class MySQLCharset: DbCharset {
        override fun getUTF8(): String {
            return "utf8mb4"
        }

        override fun getGB18030(): String {
            return "gb18030"
        }

        override fun getGBK(): String {
            return "gbk"
        }

        override fun getGB2312(): String {
            return "gb2312"
        }
    }
}