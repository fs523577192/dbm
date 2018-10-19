package org.firas.dbm.service

import org.firas.dbm.dto.TableDTO

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
interface TableManager {

    fun getById(id: String): TableDTO

    fun findBySchema(schemaId: String): List<TableDTO>

    fun create(input: TableDTO): TableDTO

    fun update(input: TableDTO)

    fun remove(id: String)
}