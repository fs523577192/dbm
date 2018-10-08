package org.firas.dbm.bo

/**
 * 数据库schema
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class Schema(val name: String, var database: Database? = null,
                  var tableMap: Map<String, Table>? = null)