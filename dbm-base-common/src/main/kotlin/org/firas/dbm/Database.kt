package org.firas.dbm.bo

import org.firas.common.util.getJsonConverter
import org.firas.common.util.safeEquals
import org.firas.common.util.safeHashCode
import org.firas.dbm.dialect.DbDialect

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
                safeEquals(host, other.host) && safeEquals(port, other.port)
    }

    override fun hashCode(): Int {
        return dbDialect.hashCode() + name.hashCode() * 97 +
                safeHashCode(host) * 89 + safeHashCode(port) * 83
    }

    override fun toString(): String {
        return "Database{dbDialect=" + this.dbDialect.toString() +
                ", name=${name}, host=${host}, port=${port}, attributes=" +
                getJsonConverter().stringify(this.attributes) + "}"
    }
}
