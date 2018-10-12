package org.firas.dbm.dto

import org.firas.common.dto.DtoBase
import org.firas.dbm.bo.Schema

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
class SchemaDTO(val recId: String? = null,
                val name: String,
                val databaseId: String? = null): DtoBase<Schema> {

    override fun toBO(): Schema {
        return Schema(name)
    }
}