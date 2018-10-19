package org.firas.dbm.service.impl

import org.firas.common.util.getUuidAsHexString
import org.firas.common.util.safeEquals
import org.firas.dbm.dao.SchemaDAO
import org.firas.dbm.dto.SchemaDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.DatabasePO
import org.firas.dbm.po.SchemaPO
import org.firas.dbm.service.DatabaseManager
import org.firas.dbm.service.SchemaManager

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月19日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
open class SchemaManagerImpl: SchemaManager {

    override fun getById(id: String): SchemaDTO {
        return schemaDAO!!.findById(id).orElseGet {
            throw EntityNotExistException("该ID的Schema不存在：$id")
        }.toDTO()
    }

    override fun findByDatabase(databaseId: String): List<SchemaDTO> {
        return schemaDAO!!.findAll { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<DatabasePO>("database").get<String>("recId"), databaseId)
        }.map { it.toDTO() }
    }

    override fun create(input: SchemaDTO): SchemaDTO {
        val databaseId = input.databaseId ?: throw IllegalArgumentException("Schema信息的数据库ID不能为空")
        val database = databaseManager!!.getById(databaseId)
        val schemaPO = SchemaPO(input)
        schemaPO.recId = getUuidAsHexString()
        schemaPO.database = DatabasePO(database)
        return schemaDAO!!.save(schemaPO).toDTO()
    }

    override fun update(input: SchemaDTO) {
        val recId = input.recId ?: throw IllegalArgumentException("Schema信息的ID不能为空")
        val databaseId = input.databaseId ?: throw IllegalArgumentException("Schema信息的数据库ID不能为空")
        val schemaPO = schemaDAO!!.findById(recId)
        if (!schemaPO.isPresent) {
            throw EntityNotExistException("该ID的Schema不存在：$recId")
        }
        val schemaNotNull = schemaPO.get()
        if (!safeEquals(databaseId, schemaNotNull.database!!.recId)) {
            val database = databaseManager!!.getById(databaseId)
            schemaNotNull.database = DatabasePO(database)
        }
        if (!safeEquals(input.name, schemaNotNull.name)) {
            schemaNotNull.name = input.name
        }
        schemaDAO!!.save(schemaNotNull)
    }

    var databaseManager: DatabaseManager? = null
    var schemaDAO: SchemaDAO? = null
}