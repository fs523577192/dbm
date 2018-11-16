package org.firas.dbm.service.impl

import org.firas.common.bo.CommonStatus
import org.firas.common.util.getUuidAsHexString
import org.firas.dbm.dao.ColumnInIndexDAO
import org.firas.dbm.dao.IndexDAO
import org.firas.dbm.dto.ColumnInIndexDTO
import org.firas.dbm.dto.IndexDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.ColumnInIndexPO
import org.firas.dbm.po.IndexPO
import org.firas.dbm.po.TablePO
import org.firas.dbm.service.IndexManager
import java.util.*
import kotlin.collections.ArrayList

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
open class IndexManagerImpl: IndexManager {

    override fun getById(id: String): IndexDTO {
        return indexDAO!!.findById(id).orElseGet {
            throw EntityNotExistException("该ID的数据库索引不存在：$id")
        }.toDTO()
    }

    override fun findByTable(tableId: String): List<IndexDTO> {
        return indexDAO!!.findAll { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<TablePO>("table").get<String>("recId"), tableId)
        }.map { it.toDTO() }
    }

    override fun create(input: Iterable<IndexDTO>): List<IndexDTO> {
        val now = Date()
        val result = ArrayList<IndexDTO>()
        indexDAO!!.saveAll(input.map {
            val indexPO = IndexPO(it)
            indexPO.recId = getUuidAsHexString()
            indexPO.createTime = now
            indexPO.table = TablePO(it.tableId)
            result.add(indexPO.toDTO())
            indexPO
        })
        return result
    }

    override fun create(input: IndexDTO): IndexDTO {
        val now = Date()
        val indexPO = IndexPO(input)
        indexPO.recId = getUuidAsHexString()
        indexPO.createTime = now
        indexPO.table = TablePO(input.tableId)
        val result = indexDAO!!.save(indexPO).toDTO()

        result.columns = ArrayList()
        input.columns?.forEach { column ->
            val toSave = ColumnInIndexDTO(result.recId!!, column.columnId,
                    column.ordinal, column.length, column.direction)
            result.columns!!.add(toSave)
        }
        result.columns = columnInIndexDAO!!.saveAll(result.columns!!.map { ColumnInIndexPO(it) })
                .mapTo(ArrayList()) { it.toDTO() }
        return result
    }

    override fun update(input: IndexDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(id: String) {
        val indexPO = indexDAO!!.findById(id)
        if (!indexPO.isPresent) {
            throw EntityNotExistException("该ID的数据库索引不存在：$id")
        }
        val indexNotNull = indexPO.get()
        indexNotNull.status = CommonStatus.DELETED.toCode()
        indexDAO!!.save(indexNotNull)
    }

    var indexDAO: IndexDAO? = null
    var columnInIndexDAO: ColumnInIndexDAO? = null
}