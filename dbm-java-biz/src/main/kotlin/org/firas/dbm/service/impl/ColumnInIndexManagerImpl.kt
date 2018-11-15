package org.firas.dbm.service.impl

import org.firas.dbm.dao.ColumnInIndexDAO
import org.firas.dbm.dto.ColumnInIndexDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.ColumnInIndexPO
import org.firas.dbm.po.ColumnPO
import org.firas.dbm.po.IndexColumnId
import org.firas.dbm.po.IndexPO
import org.firas.dbm.service.ColumnInIndexManager
import java.util.*

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
class ColumnInIndexManagerImpl: ColumnInIndexManager {

    override fun getById(indexId: String, columnId: String): ColumnInIndexDTO {
        return columnInIndexDAO!!.findById(IndexColumnId(indexId, columnId)).orElseGet {
            throw EntityNotExistException("该索引中的该ID的列不存在：" +
                    "{\"indexId\":\"$indexId\",\"columnId\":\"$columnId\"}")
        }.toDTO()
    }

    override fun findByIndex(indexId: String): List<ColumnInIndexDTO> {
        return columnInIndexDAO!!.findAll { root, query, criteriaBuilder ->
            criteriaBuilder.equal(root.get<IndexPO>("index").get<String>("recId"), indexId)
        }.map { it.toDTO() }
    }

    override fun create(input: Iterable<ColumnInIndexDTO>): List<ColumnInIndexDTO> {
        val result = ArrayList<ColumnInIndexDTO>()
        columnInIndexDAO!!.saveAll(input.map {
            val columnInIndexPO = ColumnInIndexPO(it)
            columnInIndexPO.index = IndexPO(it.indexId)
            columnInIndexPO.column = ColumnPO(it.columnId)
            result.add(columnInIndexPO.toDTO())
            columnInIndexPO
        })
        return result
    }

    override fun update(input: ColumnInIndexDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(indexId: String, columnId: String) {
        columnInIndexDAO!!.deleteById(IndexColumnId(indexId, columnId))
    }

    val columnInIndexDAO: ColumnInIndexDAO? = null
}