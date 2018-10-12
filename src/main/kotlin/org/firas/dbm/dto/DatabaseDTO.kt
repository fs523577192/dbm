package org.firas.dbm.dto

import org.firas.common.dto.DtoBase
import org.firas.dbm.bo.Database
import org.firas.dbm.dialect.DbDialect

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月11日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class DatabaseDTO(val recId: String?, val dbDialect: DbDialect, val name: String,
                  val attributes: Map<String, Any> = HashMap(),
                  var host: String? = null, var port: Int? = null):
        DtoBase<Database> {

    override fun toBO(): Database {
        return Database(dbDialect, name, attributes, HashMap(), host, port)
    }
}