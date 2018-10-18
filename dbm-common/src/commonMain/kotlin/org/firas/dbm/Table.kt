package org.firas.dbm.bo

import org.firas.common.util.getJsonConverter
import org.firas.common.util.safeEquals
import org.firas.common.util.safeHashCode
import kotlin.collections.*

/**
 * 数据库表
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class Table(val name: String, val comment: String = "", var schema: Schema? = null,
                 val attributes: Map<String, Any> = HashMap(),
                 var columnMap: LinkedHashMap<String, Column> = LinkedHashMap(),
                 var indexMap: Map<String, Index> = HashMap()) {

    override fun equals(other: Any?): Boolean {
        if (other !is Table) {
            return false
        }
        return name.equals(other.name) && safeEquals(schema, other.schema)
    }

    override fun hashCode(): Int {
        return this.name.hashCode() + 97 * safeHashCode(schema)
    }

    override fun toString(): String {
        return "Table{name=${name}, comment=${comment}, schema=" +
                this.schema?.toString() + ", attributes=" +
                getJsonConverter().stringify(this.attributes) + "}"
    }
}