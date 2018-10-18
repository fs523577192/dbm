package org.firas.dbm.bo

import org.firas.common.util.safeEquals
import org.firas.common.util.safeHashCode
import kotlin.collections.*

/**
 * 数据库schema
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class Schema(val name: String, var database: Database? = null,
                  var tableMap: Map<String, Table> = HashMap()) {

    override fun equals(other: Any?): Boolean {
        if (other !is Schema) {
            return false
        }
        return name.equals(other.name) && safeEquals(database, other.database)
    }

    override fun hashCode(): Int {
        return name.hashCode() + safeHashCode(database) * 97
    }

    override fun toString(): String {
        val db = this.database?.toString()
        return "Schema{database=${db}, name=${name}}"
    }
}