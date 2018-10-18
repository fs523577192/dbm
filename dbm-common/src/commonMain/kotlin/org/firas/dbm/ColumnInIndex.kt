package org.firas.dbm.bo

/**
 *
 *
 * <b>Creation Time:</b> 2018年10月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class ColumnInIndex(val index: Index, val column: Column, val length: Int? = null) {
}