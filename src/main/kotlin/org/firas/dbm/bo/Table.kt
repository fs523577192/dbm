package org.firas.dbm.bo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*
import kotlin.collections.LinkedHashMap

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
        return name.equals(other.name) && Objects.equals(schema, other.schema)
    }

    override fun hashCode(): Int {
        return this.name.hashCode() + 97 * Objects.hashCode(schema)
    }

    override fun toString(): String {
        return "Table{name=%s, comment=%s, schema=%s, attributes=%s}".format(
                this.name, this.comment, this.schema?.toString(),
                jacksonObjectMapper().writeValueAsString(this.attributes))
    }
}