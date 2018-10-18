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
class Index(val type: IndexType, val name: String?,
                 columnList: List<ColumnInIndex>? = null) {

    var table: Table? = null

    var columnList: List<ColumnInIndex>? = columnList
        set(value) {
            if (null != value) {
                table = value[0].column.table
                if (value.any { column -> !column.column.table!!.equals(table) }) {
                    throw IllegalArgumentException("列不都在同一个表中")
                }
            }
            field = value
        }
}