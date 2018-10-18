package org.firas.dbm.service.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.firas.common.util.getUuidAsHexString
import org.firas.common.util.safeEquals
import org.firas.dbm.dao.DatabaseDAO
import org.firas.dbm.dto.DatabaseDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.DatabasePO
import org.firas.dbm.service.DatabaseManager

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
open class DatabaseManagerImpl: DatabaseManager {

    override fun listAll(): List<DatabaseDTO> {
        return databaseDAO!!.findAll().map { it.toDTO() }
    }

    override fun create(input: DatabaseDTO): DatabaseDTO {
        val databasePO = DatabasePO(input)
        databasePO.recId = getUuidAsHexString()
        return databaseDAO!!.save(databasePO).toDTO()
    }

    override fun update(input: DatabaseDTO) {
        val databasePO = databaseDAO!!.findById(input.recId)
        if (!databasePO.isPresent) {
            throw EntityNotExistException("该ID的数据库不存在：${input.recId}")
        }
        val databaseNotNull = databasePO.get()
        if (!safeEquals(input.name, databaseNotNull.name)) {
            databaseNotNull.name = input.name
        }
        if (!safeEquals(input.host, databaseNotNull.host)) {
            databaseNotNull.host = input.host
        }
        if (!safeEquals(input.port, databaseNotNull.port)) {
            databaseNotNull.port = input.port
        }
        val dbDialect = input.dbDialect.toString()
        if (!safeEquals(dbDialect, databaseNotNull.dbDialect)) {
            databaseNotNull.dbDialect = dbDialect
        }
        databaseNotNull.attributes = jacksonObjectMapper()
                .writeValueAsString(input.attributes)
        databaseDAO!!.save(databaseNotNull)
    }

    /*
    override fun remove(id: String) {
        val databasePO = databaseDAO!!.findById(id)
        if (!databasePO.isPresent) {
            throw EntityNotExistException("该ID的数据库不存在：$id")
        }
        databaseDAO!!.delete(databasePO.get())
    }
    */

    var databaseDAO: DatabaseDAO? = null
}