package org.firas.dbm.dto

import org.firas.dbm.bo.IndexType

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class IndexDTO(val type: IndexType, val name: String?,
               val tableId: String? = null) {
}