package org.firas.dbm.dialect

import org.firas.dbm.bo.Column
import org.firas.dbm.bo.Database
import org.firas.dbm.bo.Schema
import org.firas.dbm.domain.ColumnAddition
import org.firas.dbm.domain.ColumnComment
import org.firas.dbm.domain.ColumnDrop
import org.firas.dbm.domain.ColumnModification
import org.firas.dbm.domain.ColumnRename
import org.firas.dbm.domain.TableCreation
import org.firas.dbm.type.DbType
import java.sql.Connection
import java.util.function.BiConsumer
import java.util.function.Supplier

/**
 * 数据库方言
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class DbDialect {

    /**
     * @return 用来检查数据库连接是否有效的查询SQL
     */
    abstract fun validateQuery(): String

    abstract fun getNameQuote(): String

    abstract fun getCharset(): DbCharset

    abstract fun toSQL(dbType: DbType): String

    open fun toSQL(column: Column): String {
        val dbType = column.dbType
        return "%s%s%s %s %sNULL DEFAULT %s %s".format(
                getNameQuote(), column.name, getNameQuote(),
                toSQL(dbType),
                if (column.nullable) "" else "NOT ",
                column.defaultValue,
                if (null == column.onUpdateValue) "" else "ON UPDATE %s".format(column.onUpdateValue)
        )
    }

    abstract fun toSQL(columnComment: ColumnComment): String

    open fun toSQL(columnAddition: ColumnAddition): String {
        val column = columnAddition.column
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s ADD COLUMN %s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                toSQL(column)
        )
    }

    abstract fun toSQL(columnRename: ColumnRename): String

    open fun toSQL(columnModification: ColumnModification): String {
        val column = columnModification.column
        val newColumn = Column(columnModification.dbType, column.name,
                columnModification.nullable, columnModification.defaultValue,
                columnModification.onUpdateValue)
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s MODIFY COLUMN %s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                toSQL(newColumn)
        )
    }

    open fun toSQL(columnDrop: ColumnDrop): String {
        val column = columnDrop.column
        val table = column.table
        val schema = table!!.schema
        return "ALTER TABLE %s%s%s.%s%s%s DROP COLUMN %s%s%s".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                getNameQuote(), column.name, getNameQuote()
        )
    }

    open fun toSQL(tableCreation: TableCreation): String {
        val table = tableCreation.table
        val schema = table.schema
        val builder = StringBuilder("CREATE TABLE %s%s%s.%s%s%s %s(".format(
                getNameQuote(), schema!!.name, getNameQuote(),
                getNameQuote(), table.name, getNameQuote(),
                if (tableCreation.ifNotExists) "IF NOT EXISTS " else ""
        ))
        return builder.append(table.columnMap!!.values.stream().collect(
                Supplier<MutableList<String>> { ArrayList() },
                BiConsumer<MutableList<String>, Column> { list, column -> list.add(toSQL(column))},
                BiConsumer { accumulated, newOne -> accumulated.addAll(newOne) })
                .joinToString(transform = {str -> str}))
                .append(")").toString()
    }

    abstract fun getConnection(database: Database, userName: String, password: String): Connection

    abstract fun fetchInfo(schema: Schema, userName: String, password: String): Schema
}
