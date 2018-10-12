package org.firas.dbm.po

import org.firas.common.po.PoBase
import org.firas.common.util.hashMapSizeFor
import org.firas.dbm.bo.Database
import org.firas.dbm.bo.Schema
import org.firas.dbm.bo.Table
import org.firas.dbm.dto.SchemaDTO

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
data class SchemaPO(var recId: String? = null,
                    var name: String? = null,
                    var database: DatabasePO? = null,
                    var tableCollection: Collection<TablePO>? = null):
        PoBase<Schema, SchemaDTO> {

    constructor(schema: SchemaDTO): this(
            schema.recId,
            schema.name
    )

    constructor(schema: Schema): this(
            null,
            schema.name,
            null,
            null
    )

    override fun toBO(): Schema {
        return toBO(this.database?.toBO())
    }

    internal fun toBO(database: Database?): Schema {
        val schema = Schema(name!!, database)
        val tableCollection = this.tableCollection
        if (null != tableCollection) {
            val hashMap = HashMap<String, Table>(hashMapSizeFor(tableCollection.size))
            tableCollection.forEach { table -> hashMap.put(table.name!!, table.toBO(schema)) }
            schema.tableMap = hashMap
        }
        if (null != database) {
            val hashMap = HashMap<String, Schema>(hashMapSizeFor(
                    database.schemaMap.size + 1))
            hashMap.put(schema.name, schema)
            hashMap.putAll(database.schemaMap)
            database.schemaMap = hashMap
        }
        return schema
    }

    override fun toDTO(): SchemaDTO {
        return SchemaDTO(recId, name!!, database?.recId)
    }
}