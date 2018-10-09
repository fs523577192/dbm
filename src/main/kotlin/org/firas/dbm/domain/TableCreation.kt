package org.firas.dbm.domain

import org.firas.dbm.bo.Table

/**
 * 创建数据库表
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class TableCreation(val table: Table, val ifNotExists: Boolean = false)