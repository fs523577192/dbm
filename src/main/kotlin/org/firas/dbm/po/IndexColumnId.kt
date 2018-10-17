package org.firas.dbm.po

import java.io.Serializable
import javax.persistence.Embeddable

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
data class IndexColumnId(var indexId: String? = null, var columnId: String? = null): Serializable
