package org.firas.dbm.service.impl

import org.firas.common.bo.CommonStatus
import org.firas.common.util.getUuidAsHexString
import org.firas.common.util.safeEquals
import org.firas.dbm.dao.TableDAO
import org.firas.dbm.dto.TableDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.SchemaPO
import org.firas.dbm.po.TablePO
import org.firas.dbm.service.ColumnManager
import org.firas.dbm.service.IndexManager
import org.firas.dbm.service.SchemaManager
import org.firas.dbm.service.TableManager

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
open class TableManagerImpl: TableManager {

    override fun getById(id: String): TableDTO {
        return tableDAO!!.findById(id).orElseGet {
            throw EntityNotExistException("该ID的数据库表不存在：$id")
        }.toDTO()
    }

    override fun findBySchema(schemaId: String): List<TableDTO> {
        return tableDAO!!.findAll { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<SchemaPO>("schema").get<String>("recId"), schemaId)
        }.map { it.toDTO() }
    }

    override fun create(input: TableDTO): TableDTO {
        val schema = schemaManager!!.getById(input.schemaId)
        val tablePO = TablePO(input)
        tablePO.recId = getUuidAsHexString()
        tablePO.schema = SchemaPO(schema)
        return tableDAO!!.save(tablePO).toDTO()
    }

    override fun update(input: TableDTO) {
        val recId = input.recId ?: throw IllegalArgumentException("Schema信息的ID不能为空")
        val tablePO = tableDAO!!.findById(recId)
        if (!tablePO.isPresent) {
            throw EntityNotExistException("该ID的Schema不存在：$recId")
        }
        val tableNotNull = tablePO.get()
        if (!safeEquals(input.schemaId, tableNotNull.schema!!.recId)) {
            val schema = schemaManager!!.getById(input.schemaId)
            tableNotNull.schema = SchemaPO(schema)
        }
        if (!safeEquals(input.name, tableNotNull.name)) {
            tableNotNull.name = input.name
        }
        tableDAO!!.save(tableNotNull)
    }

    override fun remove(id: String) {
        val tablePO = tableDAO!!.findById(id)
        if (!tablePO.isPresent) {
            throw EntityNotExistException("该ID的数据库表不存在：$id")
        }
        val tableNotNull = tablePO.get()
        tableNotNull.status = CommonStatus.DELETED.toCode()
        tableDAO!!.save(tableNotNull)
    }

    var schemaManager: SchemaManager? = null
    var columnManager: ColumnManager? = null
    var indexManager: IndexManager? = null
    var tableDAO: TableDAO? = null
}