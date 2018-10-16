package org.firas.dbm.po

import org.firas.common.bo.CommonStatus
import org.firas.dbm.bo.Column
import org.firas.dbm.bo.Table
import org.firas.dbm.dto.ColumnDTO
import org.firas.dbm.type.toDbType
import java.util.*

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月08日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
data class ColumnPO(var recId: String? = null,
                    var status: String? = null,
                    var name: String? = null,
                    var comment: String? = null,
                    var dbType: String? = null,
                    var nullable: Boolean? = null,
                    var defaultValue: String? = null,
                    var onUpdateValue: String? = null,
                    var createTime: Date? = null,
                    var table: TablePO? = null) {

    constructor(column: ColumnDTO): this(
            column.recId,
            column.status,
            column.name,
            column.comment,
            column.dbType.toString(),
            column.nullable,
            column.defaultValue,
            column.onUpdateValue
    )

    constructor(column: Column): this(
            null,
            CommonStatus.NORMAL.toCode(),
            column.name,
            column.comment,
            column.dbType.toString(),
            column.nullable,
            column.defaultValue,
            column.onUpdateValue,
            Date(),
            if (null == column.table) null else TablePO(column.table!!)
    )

    fun toBO(): Column {
        return toBO(this.table?.toBO())
    }

    internal fun toBO(table: Table?): Column {
        val column = Column(toDbType(this.dbType!!), this.name!!, this.nullable!!,
                "%s".format(this.defaultValue), this.onUpdateValue,
                this.comment?:"", table)
        if (null != table) {
            table.columnMap.put(column.name, column)
        }
        return column
    }
}