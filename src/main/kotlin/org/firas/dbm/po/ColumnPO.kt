package org.firas.dbm.po

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
data class ColumnPO(var id: String? = null,
                    var name: String? = null,
                    var dbType: String? = null,
                    var nullable: Boolean? = null,
                    var defaultValue: String? = null) {

}