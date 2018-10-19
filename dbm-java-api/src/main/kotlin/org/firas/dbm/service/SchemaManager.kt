package org.firas.dbm.service

import org.firas.dbm.dto.SchemaDTO

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
interface SchemaManager {

    fun getById(id: String): SchemaDTO

    fun findByDatabase(databaseId: String): List<SchemaDTO>

    fun create(input: SchemaDTO): SchemaDTO

    fun update(input: SchemaDTO)
}