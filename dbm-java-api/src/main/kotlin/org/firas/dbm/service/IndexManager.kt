package org.firas.dbm.service

import org.firas.dbm.dto.IndexDTO

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
interface IndexManager {

    fun getById(id: String): IndexDTO

    fun findByTable(tableId: String): List<IndexDTO>

    fun create(input: Iterable<IndexDTO>): List<IndexDTO>

    fun create(input: IndexDTO): IndexDTO

    fun update(input: IndexDTO)

    fun remove(id: String)
}