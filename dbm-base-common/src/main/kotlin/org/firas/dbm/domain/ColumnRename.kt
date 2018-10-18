package org.firas.dbm.domain

import org.firas.dbm.bo.Column

/**
 * 重命名数据库列
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class ColumnRename(val column: Column, val newName: String)