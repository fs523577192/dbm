package org.firas.dbm.bo

import org.firas.dbm.type.DbType

/**
 * 数据库表中的列
 *
 * <b>Creation Time:</b> 2018-10-08
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class Column(val dbType: DbType, val name: String,
                  val nullable: Boolean = true,
                  val defaultValue: String = "NULL",
                  val onUpdateValue: String? = null,
                  val comment: String = "", var table: Table? = null)