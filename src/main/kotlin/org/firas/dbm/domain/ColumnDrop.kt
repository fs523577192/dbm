package org.firas.dbm.domain

import org.firas.dbm.bo.Column

/**
 * 移除数据库列
 *
 * <b>Creation Time:</b> 2018年10月09日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class ColumnDrop(val column: Column)