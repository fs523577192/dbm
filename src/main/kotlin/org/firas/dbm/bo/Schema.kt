package org.firas.dbm.bo

import java.util.*

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
                  var tableMap: Map<String, Table>? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Schema) {
            return false
        }
        return name.equals(other.name) && Objects.equals(database, other.database)
    }

    override fun hashCode(): Int {
        return name.hashCode() + Objects.hashCode(database) * 97
    }
}