package org.firas.common.util

import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月11日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
fun closeQuietly(connection: Connection?): Connection? {
    if (null != connection && !connection.isClosed) {
        connection.close()
    }
    return null
}

fun <T: Statement> closeQuietly(statement: T?): T? {
    if (null != statement && !statement.isClosed) {
        statement.close()
    }
    return null
}

fun closeQuietly(resultSet: ResultSet?): ResultSet? {
    if (null != resultSet && !resultSet.isClosed) {
        resultSet.close()
    }
    return null
}

fun <T: InputStream> closeQuietly(inputStream: T?): T? {
    inputStream?.close()
    return null
}

fun <T: OutputStream> closeQuietly(outputStream: T?): T? {
    outputStream?.close()
    return null
}

fun <T: Reader> closeQuietly(reader: T?): T? {
    reader?.close()
    return null
}

fun <T: Writer> closeQuietly(writer: T?): T? {
    writer?.close()
    return null
}
