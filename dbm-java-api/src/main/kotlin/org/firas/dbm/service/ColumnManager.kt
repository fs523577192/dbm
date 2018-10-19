package org.firas.dbm.service

import org.firas.dbm.dto.ColumnDTO

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月18日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface ColumnManager {

    fun getById(id: String): ColumnDTO

    fun findByTable(tableId: String): List<ColumnDTO>

    fun create(input: Iterable<ColumnDTO>): List<ColumnDTO>

    fun update(input: ColumnDTO)

    fun remove(id: String)
}