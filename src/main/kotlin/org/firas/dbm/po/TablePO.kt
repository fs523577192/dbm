package org.firas.dbm.po

import com.fasterxml.jackson.databind.ObjectMapper
import org.firas.dbm.bo.Column
import org.firas.dbm.bo.Schema
import org.firas.dbm.bo.Table
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Supplier
import kotlin.collections.LinkedHashMap

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class TablePO(var recId: String? = null,
                   var status: String? = null,
                   var name: String? = null,
                   var comment: String? = null,
                   var attributes: String = "{}",
                   var schema: SchemaPO? = null,
                   var createTime: Date? = null,
                   var columnList: List<ColumnPO>? = null) {

    fun toBO(): Table {
        return toBO(this.schema?.toBO())
    }

    internal fun toBO(schema: Schema?): Table {
        val objectMapper = ObjectMapper()
        val columnList = this.columnList
        val table = Table(name!!, comment!!, schema,
                objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                LinkedHashMap(), LinkedList())

        if (null != columnList) {
            columnList.forEach { column -> table.columnMap.put(column.name!!, column.toBO(table)) }
        }
        return table
    }
}