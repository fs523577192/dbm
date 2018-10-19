package org.firas.dbm.service

import org.firas.dbm.dto.DatabaseDTO

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
interface DatabaseManager {

    fun getById(id: String): DatabaseDTO

    fun listAll(): List<DatabaseDTO>

    fun create(input: DatabaseDTO): DatabaseDTO

    fun update(input: DatabaseDTO)

    // fun remove(id: String)
}