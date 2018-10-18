package org.firas.dbm.bo

import org.firas.common.util.safeEquals
import org.firas.common.util.safeHashCode
import org.firas.dbm.type.DbType

/**
 * 数据库表中的列
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class Column(val dbType: DbType, val name: String,
                  val nullable: Boolean = true,
                  val defaultValue: String = "NULL",
                  val onUpdateValue: String? = null,
                  val comment: String = "", var table: Table? = null) {

    override fun equals(other: Any?): Boolean {
        if (other !is Column) {
            return false
        }
        return dbType.equals(other.dbType) && name.equals(other.name) &&
                safeEquals(table, other.table)
    }

    override fun hashCode(): Int {
        return dbType.hashCode() + name.hashCode() * 97 +
                safeHashCode(table) * 89
    }
}