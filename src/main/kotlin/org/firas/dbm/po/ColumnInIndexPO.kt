package org.firas.dbm.po

import org.firas.dbm.bo.ColumnInIndex
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.Table
import org.firas.dbm.dto.ColumnInIndexDTO
import java.util.*

/**
 * Many to many relation between columns and indexes
 *
 * <b>Creation Time:</b> 2018年10月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class ColumnInIndexPO(index: IndexPO? = null, column: ColumnPO? = null,
                      var ordinal: Int = 0, var length: Int? = null) {

    var index: IndexPO? = index
        set(value) {
            field = value
            id.indexId = value?.recId
        }

    var column: ColumnPO? = column
        set(value) {
            field = value
            id.columnId = value?.recId
        }

    var id: IndexColumnId = IndexColumnId(index?.recId, column?.recId)
        set(value) {
            field = value
            if (!Objects.equals(value.indexId, this.index?.recId)) {
                this.index = null
            }
            if (!Objects.equals(value.columnId, this.column?.recId)) {
                this.column = null
            }
        }

    constructor(columnInIndex: ColumnInIndex): this(
            IndexPO(columnInIndex.index),
            ColumnPO(columnInIndex.column),
            0,
            columnInIndex.length
    )

    constructor(columnInIndex: ColumnInIndexDTO): this(
            ordinal = columnInIndex.ordinal,
            length = columnInIndex.length
    )

    fun toDTO(): ColumnInIndexDTO {
        return ColumnInIndexDTO(index!!.recId!!, column!!.recId!!, ordinal)
    }

    fun toBO(table: Table?): ColumnInIndex {
        return ColumnInIndex(index!!.toBO(table), column!!.toBO(table), length)
    }

    fun toBO(index: Index?): ColumnInIndex {
        val index = index ?: this.index!!.toBO()
        return ColumnInIndex(index, this.column!!.toBO(index.table))
    }
}
