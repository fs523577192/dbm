package org.firas.dbm.dialect

import org.firas.dbm.type.DbType

/**
 * Oracle数据库方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class OracleDialect: DbDialect {

    companion object {
        val instance = OracleDialect()
    }

    override fun getJdbcType(dbType: DbType): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateQuery(): String {
        return "SELECT 1 FROM dual"
    }

    override fun getNameQuote(): String {
        return "\""
    }

    override fun getCharset(): DbCharset {
        return charset
    }

    private val charset: DbCharset = OracleCharset()

    class OracleCharset: DbCharset {
        override fun getUTF8(): String {
            return "UTF8"
        }

        override fun getGB18030(): String {
            return "ZHS32GB18030"
        }

        override fun getGBK(): String {
            return "ZHS16GBK"
        }

        override fun getGB2312(): String {
            return "ZHS16CGB231280"
        }
    }
}