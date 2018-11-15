package org.firas.dbm.service.impl

import org.firas.common.bo.CommonStatus
import org.firas.common.util.getUuidAsHexString
import org.firas.dbm.dao.ColumnDAO
import org.firas.dbm.dto.ColumnDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.ColumnPO
import org.firas.dbm.po.TablePO
import org.firas.dbm.service.ColumnManager
import java.util.*

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
open class ColumnManagerImpl: ColumnManager {

    override fun getById(id: String): ColumnDTO {
        return columnDAO!!.findById(id).orElseGet {
            throw EntityNotExistException("该ID的数据库列不存在：$id")
        }.toDTO()
    }

    override fun findByTable(tableId: String): List<ColumnDTO> {
        return columnDAO!!.findAll { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<TablePO>("table").get<String>("recId"), tableId)
        }.map { it.toDTO() }
    }

    override fun create(input: Iterable<ColumnDTO>): List<ColumnDTO> {
        val now = Date()
        val result = ArrayList<ColumnDTO>()
        columnDAO!!.saveAll(input.mapIndexed { index, columnDTO ->
            val columnPO = ColumnPO(columnDTO, index + 1)
            columnPO.recId = getUuidAsHexString()
            columnPO.createTime = now
            columnPO.table = TablePO(columnDTO.tableId)
            result.add(columnPO.toDTO())
            columnPO
        })
        return result
    }

    override fun update(input: ColumnDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(id: String) {
        val columnPO = columnDAO!!.findById(id)
        if (!columnPO.isPresent) {
            throw EntityNotExistException("该ID的数据库列不存在：$id")
        }
        val columnNotNull = columnPO.get()
        columnNotNull.status = CommonStatus.DELETED.toCode()
        columnDAO!!.save(columnNotNull)
    }

    var columnDAO: ColumnDAO? = null
}