package org.firas.dbm.service.impl

import org.firas.common.bo.CommonStatus
import org.firas.common.util.getUuidAsHexString
import org.firas.common.util.safeEquals
import org.firas.dbm.dao.TableDAO
import org.firas.dbm.dto.ColumnDTO
import org.firas.dbm.dto.ColumnInIndexDTO
import org.firas.dbm.dto.IndexDTO
import org.firas.dbm.dto.TableDTO
import org.firas.dbm.exception.EntityNotExistException
import org.firas.dbm.po.SchemaPO
import org.firas.dbm.po.TablePO
import org.firas.dbm.service.ColumnInIndexManager
import org.firas.dbm.service.ColumnManager
import org.firas.dbm.service.IndexManager
import org.firas.dbm.service.SchemaManager
import org.firas.dbm.service.TableManager
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

    override fun create(tableDTO: TableDTO, columnList: List<ColumnDTO>,
                        indexes: Iterable<IndexDTO>):
            Triple<TableDTO, List<ColumnDTO>, Iterable<IndexDTO>> {
        val now = Date()
        val schema = schemaManager!!.getById(tableDTO.schemaId)
        val tablePO = TablePO(tableDTO)
        tablePO.recId = getUuidAsHexString()
        tablePO.createTime = now
        tablePO.schema = SchemaPO(schema)
        val table = tableDAO!!.save(tablePO)

        val result = createColumnsInTable(table, columnList)
        val nameToColumnMap = result.first
        val columnsSaved = result.second

        val indexesSaved = createIndexesInTable(table, indexes, nameToColumnMap)
        return Triple(table.toDTO(), columnsSaved, indexesSaved)
    }

    private fun createColumnsInTable(
            table: TablePO,
            columnList: List<ColumnDTO>
    ): Pair<Map<String, ColumnDTO>, List<ColumnDTO>> {
        val nameToColumnMap = HashMap<String, ColumnDTO>()
        val columnResults = columnList.map {
            val columnDTO = ColumnDTO(getUuidAsHexString(),
                    name = it.name, dbType = it.dbType,
                    comment = it.comment, nullable = it.nullable,
                    defaultValue = it.defaultValue,
                    onUpdateValue = it.onUpdateValue,
                    tableId = table.recId!!)
            nameToColumnMap.put(it.name, columnDTO)
            columnDTO
        }
        val columnsSaved = columnManager!!.create(columnResults)
        return Pair(nameToColumnMap, columnsSaved)
    }

    private fun createIndexesInTable(
            table: TablePO,
            indexes: Iterable<IndexDTO>,
            nameToColumnMap: Map<String, ColumnDTO>
    ): List<IndexDTO> {
        val columnInIndexList = ArrayList<ColumnInIndexDTO>()
        val indexesSaved = indexManager!!.create(indexes.map { original ->
            val indexDTO = IndexDTO(getUuidAsHexString(), type = original.type, name = original.name,
                    tableId = table.recId!!)
            indexDTO.columns = ArrayList(original.columns!!.size)
            original.columns!!.forEach { column ->
                val columnDTO = nameToColumnMap.get(column.columnName)
                if (null == columnDTO) {
                    throw IllegalArgumentException(
                            (if (null == original.name) "一个索引" else "索引【${original.name}】") +
                                    "中的列【${column.columnName}】不存在")
                }
                val toSave = ColumnInIndexDTO(indexDTO.recId!!, columnDTO.recId!!,
                        column.ordinal, column.length, column.direction)
                indexDTO.columns!!.add(toSave)
                columnInIndexList.add(toSave)
            }
            indexDTO
        })
        columnInIndexManager!!.create(columnInIndexList)
        return indexesSaved
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
    var columnInIndexManager: ColumnInIndexManager? = null
    var tableDAO: TableDAO? = null
}