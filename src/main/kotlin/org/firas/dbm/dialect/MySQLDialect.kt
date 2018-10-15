package org.firas.dbm.dialect

import org.firas.common.util.closeQuietly
import org.firas.dbm.bo.Column
import org.firas.dbm.bo.Database
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.Schema
import org.firas.dbm.bo.Table
import org.firas.dbm.domain.ColumnComment
import org.firas.dbm.domain.ColumnRename
import org.firas.dbm.type.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

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
        if (dbType is SmallIntType) {
            return "SMALLINT"
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

    override fun getConnection(database: Database, userName: String, password: String): Connection {
        val host = database.host
        val port = database.port
        if (null == host) {
            throw IllegalStateException("数据库地址为空")
        }
        if (null == port) {
            throw IllegalStateException("数据库端口为空")
        }
        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(
                "jdbc:mysql//%s:%d/%s".format(host, port, database.name),
                userName, password)
    }

    override fun fetchInfo(schema: Schema, userName: String, password: String): Schema {
        var connection: Connection? = null
        var statement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        try {
            val tableMap = HashMap<String, Table>()
            val tableUnsignedColumnMap = HashMap<String, MutableSet<String>>()
            val tableColumnMap = HashMap<String, LinkedHashMap<String, Column>>()
            val tableIndexMap = HashMap<String, MutableMap<String, Index>>()
            connection = getConnection(schema.database!!, userName, password)
            try {
                statement = connection.prepareStatement("SELECT " +
                        "`table_name`, `table_comment`, `engine`, `auto_increment`, `table_collation` " +
                        "FROM `information_schema`.`tables` WHERE `table_schema` = ? AND " +
                        "`table_type` IN ('TABLE', 'VIEW')")
                statement.setString(1, schema.name)
                resultSet = statement.executeQuery()
                handleTableInfo(schema, resultSet, tableMap, tableColumnMap, tableIndexMap)
            } finally {
                resultSet = closeQuietly(resultSet)
                statement = closeQuietly(statement)
            }
            tableMap.keys.forEach { queryUnsignedColumns(connection, it,
                    tableUnsignedColumnMap.computeIfAbsent(it, { HashSet() })) }
            try {
                statement = connection.prepareStatement("SELECT `table_name`, `column_name`, " +
                        "`column_default`, `is_nullable`, `data_type`, `character_maximum_length`, " +
                        "`character_octet_length`, `numeric_precision`, `numeric_scale`, " +
                        "`datetime_precision`, `character_set_name`, `collation_name`, " +
                        "`extra`, `column_comment`, `generation_expression` FROM " +
                        "`information_schema`.`columns` WHERE `table_schema` = ? " +
                        "ORDER BY `table_name`, `ordinal_position`")
                statement.setString(1, schema.name)
                resultSet = statement.executeQuery()
                handleColumnInfo(schema, resultSet, tableColumnMap, tableUnsignedColumnMap)
            } finally {
                closeQuietly(resultSet)
                closeQuietly(statement)
            }
            return Schema(schema.name, schema.database, tableMap)
        } finally {
            closeQuietly(connection)
        }
    }

    private fun queryUnsignedColumns(connection: Connection, tableName: String,
                                     unsignedSet: MutableSet<String>) {
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        try {
            statement = connection.createStatement()
            resultSet = statement.executeQuery("DESC %s".format(tableName))
            while (resultSet.next()) {
                if (resultSet.getString("type").endsWith("unsigned")) {
                    unsignedSet.add(resultSet.getString("field"))
                }
            }
        } finally {
            closeQuietly(resultSet)
            closeQuietly(statement)
        }
    }

    private fun handleTableInfo(schema: Schema, resultSet: ResultSet,
                                tableMap: MutableMap<String, Table>,
                                tableColumnMap: MutableMap<String, LinkedHashMap<String, Column>>,
                                tableIndexMap: MutableMap<String, MutableMap<String, Index>>) {
        while (resultSet.next()) {
            val name = resultSet.getString("table_name")
            val attributes = HashMap<String, Any>()
            attributes.put("engine", resultSet.getString("engine"))
            attributes.put("auto_increment", resultSet.getLong("auto_increment"))
            attributes.put("collation", resultSet.getString("table_collation"))
            tableMap.put(name, Table(name, resultSet.getString("table_comment"),
                    schema, attributes, tableColumnMap.computeIfAbsent(name, { LinkedHashMap() }),
                    tableIndexMap.computeIfAbsent(name, { HashMap() })))
        }
        tableMap.values.forEach { table -> table.columnMap.values.forEach { it.table = table } }
    }

    private fun handleColumnInfo(schema: Schema, resultSet: ResultSet,
                                 tableColumnMap: MutableMap<String, LinkedHashMap<String, Column>>,
                                 tableUnsignedColumnMap: Map<String, MutableSet<String>>) {
        while (resultSet.next()) {
            val tableName = resultSet.getString("table_name")
            val columnName = resultSet.getString("column_name")
            tableColumnMap.computeIfPresent(tableName, { tableName, columnMap ->
                val dbType = toDbType(resultSet.getString("data_type"),
                        tableUnsignedColumnMap.get(tableName)!!.contains(columnName),
                        resultSet.getInt("character_maximum_length"),
                        resultSet.getInt("numeric_precision"),
                        resultSet.getInt("numeric_scale"),
                        resultSet.getInt("datetime_precision"),
                        resultSet.getString("character_set_name"))
                val onUpdateMatch = Regex("on update (\\w+)").find(
                        "%s".format(resultSet.getString("extra")))
                val column = Column(dbType, columnName,
                        "yes".equals(resultSet.getString("is_nullable"), true),
                        "%s".format(resultSet.getString("column_default")),
                        if (null == onUpdateMatch) null else onUpdateMatch.groupValues[1],
                        resultSet.getString("column_comment"))
                columnMap.put(columnName, column)
                columnMap
            })
        }
    }

    private fun toDbType(dataType: String, unsigned: Boolean,
                         characterLength: Int,
                         precision: Int, scale: Int,
                         dateTimePrecision: Int,
                         charsetName: String?): DbType {
        return when (dataType) {
            "int" -> IntegerType(unsigned)
            "bigint" -> BigIntType(unsigned)
            "decimal" -> DecimalType(precision, scale)
            "varchar" -> VarcharType(characterLength, charsetName!!)
            "datetime" -> DateTimeType(dateTimePrecision)
            "smallint" -> SmallIntType(unsigned)
            "double" -> DoubleType()
            "float" -> FloatType()
            "text", "mediumtext", "longtext" -> ClobType()
            "blob", "mediumblob", "longblob" -> BlobType()
            else -> throw Exception("Unsupported datatype: %s".format(dataType))
        }
    }
}
