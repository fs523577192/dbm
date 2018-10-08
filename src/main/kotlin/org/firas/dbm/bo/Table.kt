package org.firas.dbm.bo

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
                 var columnMap: LinkedHashMap<String, Column>? = null)