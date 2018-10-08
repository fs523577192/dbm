package org.firas.dbm.po

import com.fasterxml.jackson.databind.ObjectMapper
import org.firas.dbm.bo.Schema

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
data class SchemaPO(var recId: String? = null,
                    var name: String? = null,
                    var database: DatabasePO? = null,
                    var tableCollection: Collection<TablePO>? = null) {

    fun toBO(): Schema {
        return Schema(name!!, null, null)
    }
}