package org.firas.dbm.dialect

import org.firas.dbm.bo.Column
import org.firas.dbm.domain.ColumnComment
import org.firas.dbm.domain.ColumnRename
import org.firas.dbm.type.*

/**
 * MySQL方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class MySQLDialect private constructor(): DbDialect() {

    companion object {
        val instance = MySQLDialect()
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

    override fun toSQL(dbType: DbType): String {
        if (dbType is VarcharType) {
            return "VARCHAR(" + dbType.length +
                    ") CHARSET " + dbType.charset
        }
        if (dbType is DecimalType) {
            return "DECIMAL(" + dbType.precision + ", " +
                    dbType.scale + ')'
        }
        if (dbType is IntegerType) {
            return if (dbType.unsigned) "UNSIGNED INT" else "INT"
        }
        if (dbType is BigIntType) {
            return if (dbType.unsigned) "UNSIGNED BIGINT" else "BIGINT"
        }
        if (dbType is DateTimeType) {
            return "DATETIME(" + dbType.fractional + ')'
        }
        if (dbType is DoubleType) {
            return "DOUBLE"
        }
        if (dbType is FloatType) {
            return "FLOAT"
        }
        if (dbType is ClobType) {
            return "LONGTEXT"
        }
        if (dbType is BlobType) {
            return "LONGBLOB"
        }
        throw IllegalArgumentException("不支持的数据类型：" +
                dbType.javaClass.name)
    }

    override fun toSQL(column: Column): String {
        return "%s COMMENT '%s'".format(
                super.toSQL(column),
                column.comment.replace("'", "''")
        )
    }

    override fun toSQL(columnComment: ColumnComment): String {
        val column = columnComment.column
        val newColumn = Column(column.dbType, column.name, column.nullable,
                column.defaultValue, column.onUpdateValue, columnComment.comment)
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s MODIFY COLUMN %s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                toSQL(newColumn))
    }

    override fun toSQL(columnRename: ColumnRename): String {
        val column = columnRename.column
        val newColumn = Column(column.dbType, columnRename.newName, column.nullable,
                column.defaultValue, column.onUpdateValue, column.comment)
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s CHANGE COLUMN %s%s%s %s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                getNameQuote(), column.name, getNameQuote(),
                toSQL(newColumn))
    }
}
