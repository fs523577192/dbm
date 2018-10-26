package org.firas.dbm.dialect

import org.firas.common.util.closeQuietly
import org.firas.dbm.bo.Column
import org.firas.dbm.bo.ColumnInIndex
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.IndexType
import org.firas.dbm.bo.Schema
import org.firas.dbm.bo.Table
import org.firas.dbm.domain.ColumnComment
import org.firas.dbm.domain.ColumnRename
import org.firas.dbm.type.*
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

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

    override fun toString(): String {
        return "oracle"
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
            return "NUMBER(10, 0)"
        }
        if (dbType is BigIntType) {
            return "NUMBER(20, 0)"
        }
        if (dbType is SmallIntType) {
            return "NUMBER(5, 0)"
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

    fun toSQL(indexAddition: IndexAddition): String {
        val index = indexAddition.index
        val table = index.table!!
        val schema = table.schema
        val quote = getNameQuote()
        val name = if (null == index.name || "" == index.name) base64Time() else index.name
        return "CREATE INDEX $quote${schema.name}$quote.$quote$name$quote ON " +
                "$quote${schema.name}$quote.$quote${table.name} (" +
                index.columnList.joinToString(transform = {
                    it.column.name + if (null == it.length) " " else "(${it.length}) " +
                            item.direction.toString()
                }) + ")"
    }

    fun toSQL(indexDrop: IndexDrop): String {
        val index = indexAddition.index
        val table = index.table!!
        val schema = table.schema
        val quote = getNameQuote()
        return "DROP INDEX $quote${schema.name}$quote.$quote${index.name}$quote"
    }

    private fun base64Time(): String {
        var time = Date().getTime()
        val array = ByteArray(8)
        for (i in 0 .. 7) {
            array[i] = time.and(0xFF).toByte()
            time = time.shr(8)
        }
        return Base64.getUrlEncoder().encodeToString(array)
    }

    override fun fetchInfo(schema: Schema, userName: String, password: String): Schema {
        var connection: Connection? = null
        try {
            val tableMap = HashMap<String, Table>()
            connection = getConnection(schema.database!!, userName, password)
            val tableCommentMap = fetchTableComment(connection)
            val tableColumnMap = fetchColumnInfo(connection)
            val tableIndexMap = fetchIndexInfo(connection, tableColumnMap)
            tableCommentMap.forEach { tableName, comment ->
                tableMap.put(tableName, Table(tableName, comment, schema,
                        HashMap(), tableColumnMap.get(tableName)!!,
                        tableIndexMap.getOrDefault(tableName, HashMap())
                )) }
            tableMap.values.forEach { table -> table.columnMap.values.forEach { it.table = table } }
            return Schema(schema.name, schema.database, tableMap)
        } finally {
            closeQuietly(connection)
        }
    }

    private fun fetchTableComment(connection: Connection): Map<String, String> {
        val tableCommentMap = HashMap<String, String>()
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        try {
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT " +
                    "a.table_name, b.comments FROM user_tables a " +
                    "LEFT JOIN user_tab_comments b " +
                    "ON a.table_name = b.table_name " +
                    "WHERE a.duration IS NULL AND " +
                    "(a.dropped IS NULL OR a.dropped = 'NO')")
            while (resultSet.next()) {
                tableCommentMap.put(resultSet.getString("table_name"),
                        resultSet.getString("comments") ?: "")
            }
            return tableCommentMap
        } finally {
            closeQuietly(resultSet)
            closeQuietly(statement)
        }
    }

    private fun fetchColumnInfo(connection: Connection): Map<String, LinkedHashMap<String, Column>> {
        val tableColumnMap = HashMap<String, LinkedHashMap<String, Column>>()
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        try {
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT a.table_name, " +
                    "a.column_name, a.data_type, a.data_length, " +
                    "a.data_precision, a.data_scale, a.nullable, " +
                    "a.data_default, b.comments FROM user_tab_columns a " +
                    "JOIN user_tables t " +
                    "ON a.table_name = t.table_name AND " +
                    "t.duration IS NULL AND " +
                    "(t.dropped IS NULL OR t.dropped = 'NO') " +
                    "LEFT JOIN user_col_comments b " +
                    "ON a.table_name = b.table_name AND " +
                    "a.column_name = b.column_name")
            while (resultSet.next()) {
                val tableName = resultSet.getString("table_name")
                val columnName = resultSet.getString("column_name")
                val dbType = toDbType(resultSet.getString("data_type"),
                        resultSet.getInt("data_length"),
                        resultSet.getInt("data_precision"),
                        resultSet.getInt("data_scale"))
                tableColumnMap.computeIfAbsent(tableName, { LinkedHashMap() })
                        .put(columnName, Column(dbType, resultSet.getString("column_name"),
                                "Y".equals(resultSet.getString("nullable"), true),
                                "%s".format(resultSet.getString("data_default")),
                                null, resultSet.getString("comments") ?: ""))
            }
        } finally {
            closeQuietly(resultSet)
            closeQuietly(statement)
        }
        return tableColumnMap
    }

    private fun fetchIndexInfo(connection: Connection,
                               tableColumnMap: Map<String, Map<String, Column>>):
            Map<String, Map<String, Index>> {
        val tableIndexMap = HashMap<String, HashMap<String, Index>>()
        val temp = HashMap<String, HashMap<String, MutableList<ColumnInIndex>>>()
        var statement: Statement? = null
        var resultSet: ResultSet? = null
        try {
            statement = connection.createStatement()
            resultSet = statement.executeQuery("SELECT a.table_name, " +
                    "a.index_name, a.column_name, a.column_length, " +
                    "a.descend, b.uniqueness, c.constraint_type " +
                    "FROM user_ind_columns a " +
                    "JOIN user_indexes b " +
                    "ON a.table_name = b.table_name AND " +
                    "a.index_name = b.index_name " +
                    "JOIN user_tables t " +
                    "ON a.table_name = t.table_name AND " +
                    "t.duration IS NULL AND " +
                    "(t.dropped IS NULL OR t.dropped = 'NO') " +
                    "LEFT JOIN user_constraints c " +
                    "ON c.table_name = a.table_name AND " +
                    "c.index_name = a.index_name AND " +
                    "c.status = 'ENABLED' " +
                    "ORDER BY a.table_name, a.index_name, a.column_position")
            while (resultSet.next()) {
                val tableName = resultSet.getString("table_name")
                val columnName = resultSet.getString("column_name")
                val indexName = resultSet.getString("index_name")
                val indexType = toIndexType(resultSet.getString("uniqueness"),
                        resultSet.getString("constraint_type"))
                val column = tableColumnMap.get(tableName)!!.get(columnName)!!
                val index = tableIndexMap.computeIfAbsent(tableName){ HashMap() }
                                .computeIfAbsent(indexName){ Index(indexType, indexName, null) }
                val columnInIndex = ColumnInIndex(index, column,
                        resultSet.getInt("column_length"))
                temp.computeIfAbsent(tableName, { HashMap() })
                        .computeIfAbsent(indexName, { ArrayList() })
                        .add(columnInIndex)
            }
            tableIndexMap.forEach { tableName, map ->
                map.forEach { indexName, index ->
                    index.columnList = temp.get(tableName)!!.get(indexName) } }
        } finally {
            closeQuietly(resultSet)
            closeQuietly(statement)
        }
        return tableIndexMap
    }

    private fun toIndexType(uniqueness: String?, constraintType: String?): IndexType {
        return if ("P".equals(constraintType, true)) IndexType.PRIMARY
        else if ("UNIQUE".equals(uniqueness, true)) IndexType.UNIQUE
        else IndexType.NORMAL
    }

    private fun toDbType(dataType: String, dataLength: Int,
                         precision: Int, scale: Int): DbType {
        return when (dataType) {
            "NUMBER" -> when (scale) {
                in Int.MIN_VALUE .. 0 -> when (if (precision <= 0) dataLength else precision) {
                    in 1 .. 4 -> SmallIntType()
                    in 5 .. 9 -> IntegerType()
                    in 10 .. 18 -> BigIntType()
                    else -> DecimalType(if (precision <= 0) dataLength else precision, 0)
                }
                else -> DecimalType(if (precision <= 0) dataLength else precision, scale)
            }
            "VARCHAR2", "VARCHAR" -> VarcharType(dataLength, getCharset().getUTF8())
            "CLOB" -> ClobType()
            "DATE" -> DateTimeType()
            "BLOB" -> BlobType()
            "BINARY_DOUBLE" -> DoubleType()
            "BINARY_FLOAT" -> FloatType()
            else -> throw Exception("Unsupported data type: %s".format(dataType))
        }
    }
}
