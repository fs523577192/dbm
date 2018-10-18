package org.firas.dbm.dialect

import org.firas.dbm.bo.Database
import java.sql.Connection
import java.sql.DriverManager

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月18日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
fun getConnection(database: Database, userName: String, password: String): Connection {
    val host = database.host
    val port = database.port
    if (null == host) {
        throw IllegalStateException("数据库地址为空")
    }
    if (null == port) {
        throw IllegalStateException("数据库端口为空")
    }
    if (database.dbDialect is OracleDialect) {
        Class.forName("oracle.jdbc.driver.OracleDriver")
        return DriverManager.getConnection(
                "jdbc:oracle:thin:@%s:%d:%s".format(host, port, database.name),
                userName, password)
    } else if (database.dbDialect is MySQLDialect) {
        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(
                "jdbc:mysql//%s:%d/%s".format(host, port, database.name),
                userName, password)
    }
    throw IllegalStateException("不支持的数据库类型")
}