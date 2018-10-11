package org.firas.dbm.dto

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
               val status: String,
               val name: String,
               val comment: String = "",
               val attributes: Map<String, Any> = HashMap(),
               val schemaId: String) {
}