package org.firas.dbm.po

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.firas.common.bo.CommonStatus
import org.firas.common.po.PoBase
import org.firas.common.util.hashMapSizeFor
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.Schema
import org.firas.dbm.bo.Table
import org.firas.dbm.dto.TableDTO
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
                   var columnList: List<ColumnPO>? = null,
                   var indexList: List<IndexPO>? = null): PoBase<Table, TableDTO> {

    constructor(table: Table): this(
            null,
            CommonStatus.NORMAL.toCode(),
            table.name,
            table.comment,
            jacksonObjectMapper().writeValueAsString(table.attributes),
            if (null == table.schema) null else SchemaPO(table.schema!!),
            Date()
    )

    constructor(table: TableDTO): this(
            table.recId,
            table.status,
            table.name,
            table.comment,
            jacksonObjectMapper().writeValueAsString(table.attributes)
    )

    override fun toBO(): Table {
        return toBO(this.schema?.toBO())
    }

    internal fun toBO(schema: Schema?): Table {
        val objectMapper = jacksonObjectMapper()
        val columnList = this.columnList
        val indexList = this.indexList
        val table = Table(name!!, comment!!, schema,
                objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                LinkedHashMap(), LinkedHashMap())

        if (null != columnList) {
            columnList.forEach { column -> table.columnMap.put(column.name!!, column.toBO(table)) }
        }
        if (null != indexList) {
            table.indexMap = indexList.stream().collect(
                    Supplier<MutableMap<String, Index>> { HashMap() },
                    BiConsumer { map, indexPO ->
                        map.put(indexPO.name!!, indexPO.toBO(table)) },
                    BiConsumer { map, newMap -> map.putAll(newMap) }
            )
        }
        if (null != schema) {
            val hashMap = HashMap<String, Table>(
                    hashMapSizeFor(schema.tableMap.size + 1))
            hashMap.putAll(schema.tableMap)
            hashMap.put(table.name, table)
            schema.tableMap = hashMap
        }
        return table
    }

    override fun toDTO(): TableDTO {
        return TableDTO(this.recId, this.status ?: CommonStatus.NORMAL.toCode(),
                this.name!!, this.comment ?: "",
                jacksonObjectMapper().readValue(this.attributes ?: "{}", Map::class.java) as Map<String, Any>,
                this.schema!!.recId!!)
    }
}