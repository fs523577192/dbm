package org.firas.dbm.dto

import org.firas.common.dto.DtoBase
import org.firas.dbm.bo.Column
import org.firas.dbm.type.DbType

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月11日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class ColumnDTO(val recId: String? = null,
                val status: String,
                val name: String,
                val comment: String = "",
                val dbType: DbType,
                val nullable: Boolean = true,
                val defaultValue: String = "NULL",
                val onUpdateValue: String? = null,
                val tableId: String? = null): DtoBase<Column> {

    override fun toBO(): Column {
        return Column(dbType, name, nullable, defaultValue, onUpdateValue, comment)
    }
}