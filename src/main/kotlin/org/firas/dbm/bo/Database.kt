package org.firas.dbm.bo

import org.firas.dbm.dialect.DbDialect

/**
 * 数据库
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class Database(val dbDialect: DbDialect, val name: String,
                    val attributes: Map<String, Any> = HashMap(),
                    var schemaMap: Map<String, Schema>? = null,
                    var host: String? = null, var port: Int? = null)
