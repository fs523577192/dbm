package org.firas.dbm.domain

import org.firas.dbm.bo.Column

/**
 * 更新数据库列的注释
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class ColumnComment(val column: Column, val comment: String)