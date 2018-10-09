package org.firas.dbm.po

import com.fasterxml.jackson.databind.ObjectMapper
import org.firas.dbm.bo.Database
import org.firas.dbm.bo.Schema
import org.firas.dbm.dialect.OracleDialect
import org.firas.dbm.dialect.MySQLDialect
import java.util.function.BiConsumer
import java.util.function.Supplier

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
                      var port: Int? = null) {

    fun toBO(): Database {
        val objectMapper = ObjectMapper()
        val schemaCollection = this.schemaCollection
        return Database(if ("oracle".equals(dbDialect, true)) OracleDialect.instance else MySQLDialect.instance,
                name!!, objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                if (null == schemaCollection) HashMap() else schemaCollection.stream().collect(
                        Supplier<MutableMap<String, Schema>> { HashMap() },
                        BiConsumer<MutableMap<String, Schema>, SchemaPO> { map, schema -> map.put(schema.name!!, schema.toBO()) },
                        BiConsumer { accumulated, newOne -> accumulated.putAll(newOne) }
                ), host, port)
    }
}