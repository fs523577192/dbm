package org.firas.dbm.po

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.firas.common.po.PoBase
import org.firas.common.util.hashMapSizeFor
import org.firas.dbm.bo.Database
import org.firas.dbm.bo.Schema
import org.firas.dbm.dialect.OracleDialect
import org.firas.dbm.dialect.MySQLDialect
import org.firas.dbm.dto.DatabaseDTO

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
data class DatabasePO(var recId: String? = null,
                      var dbDialect: String? = null,
                      var name: String? = null,
                      var attributes: String = "{}",
                      var schemaCollection: Collection<SchemaPO>? = null,
                      var host: String? = null,
                      var port: Int? = null): PoBase<Database, DatabaseDTO> {

    constructor(database: Database): this(
            null,
            database.dbDialect.toString(),
            database.name,
            jacksonObjectMapper().writeValueAsString(database.attributes),
            null,
            database.host,
            database.port
    )

    constructor(database: DatabaseDTO): this(
            database.recId,
            database.dbDialect.toString(),
            database.name,
            jacksonObjectMapper().writeValueAsString(database.attributes),
            null,
            database.host,
            database.port
    )

    override fun toDTO(): DatabaseDTO {
        val objectMapper = jacksonObjectMapper()
        return DatabaseDTO(recId,
                if ("oracle".equals(dbDialect, true)) OracleDialect.instance else MySQLDialect.instance,
                name!!, objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                host, port)
    }

    override fun toBO(): Database {
        val objectMapper = jacksonObjectMapper()
        val schemaCollection = this.schemaCollection
        val schemaMap = HashMap<String, Schema>(hashMapSizeFor(schemaCollection?.size ?: 1))
        val database = Database(if ("oracle".equals(dbDialect, true)) OracleDialect.instance else MySQLDialect.instance,
                name!!, objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                schemaMap, host, port)
        schemaCollection?.stream()?.forEach{ schema -> schemaMap.put(schema.name!!, schema.toBO(database)) }
        return database
    }
}