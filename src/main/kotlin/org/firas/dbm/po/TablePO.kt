package org.firas.dbm.po

import com.fasterxml.jackson.databind.ObjectMapper
import org.firas.dbm.bo.Table

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
data class TablePO(var id: String? = null,
                   var name: String? = null,
                   var comment: String? = null,
                   var attributes: String = "{}",
                   var schema: SchemaPO? = null,
                   var columnList: List<ColumnPO>? = null) {

    fun toBO(): Table {
        val objectMapper = ObjectMapper()
        return Table(name!!, comment!!, null,
                objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>, null)
    }
}