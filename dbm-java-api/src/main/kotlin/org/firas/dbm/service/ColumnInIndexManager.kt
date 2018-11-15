package org.firas.dbm.service

import org.firas.dbm.dto.ColumnInIndexDTO

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年11月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
interface ColumnInIndexManager {

    fun getById(indexId: String, columnId: String): ColumnInIndexDTO

    fun findByIndex(indexId: String): List<ColumnInIndexDTO>

    fun create(input: Iterable<ColumnInIndexDTO>): List<ColumnInIndexDTO>

    fun update(input: ColumnInIndexDTO)

    fun remove(indexId: String, columnId: String)
}