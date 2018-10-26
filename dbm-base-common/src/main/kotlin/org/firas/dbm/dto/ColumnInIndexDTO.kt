package org.firas.dbm.dto

/**
 * <p/>
 * 索引中的列
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class ColumnInIndexDTO(val indexId: String, val columnId: String,
                       val ordinal: Int = 0, var length: Int? = null,
                       val direction: SortDirection = SortDirection.ASC) {
}
