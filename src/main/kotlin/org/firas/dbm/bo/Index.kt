package org.firas.dbm.bo

/**
 * 数据库索引
 *
 * <b>Creation Time:</b> 2018年10月09日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class Index(val type: IndexType, val name: String?, val columnList: List<Column>) {

    var table: Table? = null

    init {
        table = columnList[0].table
        if (columnList.any { column -> !column.table!!.equals(table) }) {
            throw IllegalArgumentException("列不都在同一个表中")
        }
    }
}