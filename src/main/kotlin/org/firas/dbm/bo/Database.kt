package org.firas.dbm.bo

import org.firas.dbm.dialect.DbDialect
import org.firas.dbm.dialect.OracleDialect
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * 数据库
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class Database(val dbDialect: DbDialect, val name: String,
                    val attributes: Map<String, Any> = HashMap(),
                    var schemaMap: Map<String, Schema> = HashMap(),
                    var host: String? = null, var port: Int? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Database) {
            return false
        }
        return dbDialect.equals(other.dbDialect) && name.equals(other.name) &&
                Objects.equals(host, other.host) && Objects.equals(port, other.port)
    }

    override fun hashCode(): Int {
        return dbDialect.hashCode() + name.hashCode() * 97 +
                Objects.hashCode(host) * 89 + Objects.hashCode(port) * 83
    }

    fun getConnection(userName: String, password: String): Connection {
        val host = this.host
        val port = this.port
        if (null == host) {
            throw IllegalStateException("数据库地址为空")
        }
        if (null == port) {
            throw IllegalStateException("数据库端口为空")
        }
        if (dbDialect is OracleDialect) {
            Class.forName("oracle.jdbc.driver.OracleDriver")
            return DriverManager.getConnection(
                    "jdbc:oracle:thin:@%s:%d:%s".format(host, port, name),
                    userName, password)
        }
        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(
                "jdbc:mysql//%s:%d/%s".format(host, port, name),
                userName, password)
    }
}
