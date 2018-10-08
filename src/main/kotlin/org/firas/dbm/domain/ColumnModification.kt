package org.firas.dbm.domain

import org.firas.dbm.bo.Column
import org.firas.dbm.type.DbType

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
data class ColumnModification(val column: Column, val dbType: DbType,
                              val nullable: Boolean, val defaultValue: String?)