package org.firas.dbm.po

import com.fasterxml.jackson.databind.ObjectMapper
import org.firas.dbm.bo.Database
import org.firas.dbm.dialect.OracleDialect
import org.firas.dbm.dialect.MySQLDialect

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
        return Database(if ("oracle".equals(dbDialect, true)) OracleDialect.instance else MySQLDialect.instance,
                name!!, objectMapper.readValue(attributes, Map::class.java) as Map<String, Any>,
                null, host, port)
    }
}