package org.firas.dbm.bo

import org.firas.dbm.dialect.DbDialect
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
}
