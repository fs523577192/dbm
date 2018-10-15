package org.firas.dbm.po

import org.firas.dbm.bo.ColumnInIndex
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

    var indexId: String? = index?.recId
        set(value) {
            if (Objects.equals(value, index?.recId)) {
                this.index = null
            }
            field = value
        }

    var columnId: String? = column?.recId
        set(value) {
            if (Objects.equals(value, column?.recId)) {
                this.column = null
            }
            field = value
        }

    var index: IndexPO? = index
        set(value) {
            field = value
            this.indexId = value?.recId
        }

    var column: ColumnPO? = null
        set(value) {
            field = value
            this.columnId = value?.recId
        }

    fun toDTO(): ColumnInIndexDTO {
        return ColumnInIndexDTO(index!!.recId!!, column!!.recId!!, ordinal)
    }

    fun toBO(table: Table?): ColumnInIndex {
        return ColumnInIndex(index!!.toBO(table), column!!.toBO(table), length)
    }
}