package org.firas.dbm.dto

import org.firas.common.bo.CommonStatus
import org.firas.common.dto.DtoBase
import org.firas.dbm.bo.Table
import kotlin.collections.HashMap

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
class TableDTO(val recId: String? = null,
               val status: String = CommonStatus.NORMAL.toCode(),
               val name: String,
               val comment: String = "",
               val attributes: Map<String, Any> = HashMap(),
               val schemaId: String): DtoBase<Table> {

    override fun toBO(): Table {
        return Table(name, comment, null, attributes)
    }
}