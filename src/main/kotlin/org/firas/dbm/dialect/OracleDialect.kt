package org.firas.dbm.dialect

import org.firas.dbm.domain.ColumnComment
import org.firas.dbm.domain.ColumnRename
import org.firas.dbm.type.*

/**
 * Oracle数据库方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class OracleDialect: DbDialect() {

    companion object {
        val instance = OracleDialect()
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

    override fun toSQL(dbType: DbType): String {
        if (dbType is VarcharType) {
            return "VARCHAR2(" + dbType.length + ')'
        }
        if (dbType is DecimalType) {
            return "NUMBER(" + dbType.precision + ", " +
                    dbType.scale + ')'
        }
        if (dbType is IntegerType) {
            return "INT"
        }
        if (dbType is BigIntType) {
            return "BIGINT"
        }
        if (dbType is DateTimeType) {
            return "DATE(" + dbType.fractional + ')'
        }
        if (dbType is DoubleType) {
            return "BINARY_DOUBLE"
        }
        if (dbType is FloatType) {
            return "BINARY_FLOAT"
        }
        if (dbType is ClobType) {
            return "CLOB"
        }
        if (dbType is BlobType) {
            return "BLOB"
        }
        throw IllegalArgumentException("不支持的数据类型：" +
                dbType.javaClass.name)
    }

    override fun toSQL(columnComment: ColumnComment): String {
        val column = columnComment.column
        val table = column.table
        val schema = table!!.schema
        return "COMMENT ON COLUMN %s%s%s.%s%s%s.%s%s%s IS '%s'".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                getNameQuote(), column.name, getNameQuote(),
                columnComment.comment.replace("'", "''"))
    }

    override fun toSQL(columnRename: ColumnRename): String {
        val column = columnRename.column
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s RENAME COLUMN %s%s%s TO %s%s%s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                getNameQuote(), column.name, getNameQuote(),
                getNameQuote(), columnRename.newName, getNameQuote()
        )
    }
}
