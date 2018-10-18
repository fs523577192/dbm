package org.firas.dbm.po

import org.firas.common.bo.CommonStatus
import org.firas.common.po.PoBase
import org.firas.dbm.bo.ColumnInIndex
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.IndexType
import org.firas.dbm.bo.Table
import org.firas.dbm.dto.IndexDTO
import java.util.*
import java.util.stream.Collectors

/**
 * <b><code></code></b>
 * <p/>
 *
 * <p/>
 *
 * <b>Creation Time:</b> 2018年10月15日
 *
 * @author Wu Yuping
 * @version 1.0.0
 * @since 1.0.0
 */
class IndexPO(var recId: String? = null,
              var status: String? = null,
              var indexType: Int = 0,
              var name: String? = null,
              var createTime: Date? = null,
              var table: TablePO? = null,
              var columnList: List<ColumnInIndexPO>? = null): PoBase<Index, IndexDTO> {

    constructor(index: Index): this(
            null, 
            CommonStatus.NORMAL.toCode(),
            index.type.ordinal,
            index.name,
            Date(),
            if (null == index.table) null else TablePO(index.table!!)
    )

    constructor(index: IndexDTO): this(
            index.recId,
            index.status,
            index.type.ordinal,
            index.name
    )

    override fun toDTO(): IndexDTO {
        return IndexDTO(recId, status ?: CommonStatus.NORMAL.toCode(),
                IndexType.values().get(indexType), name, table?.recId)
    }

    override fun toBO(): Index {
        return toBO(null)
    }

    fun toBO(table: Table?): Index {
        val index = Index(IndexType.values().get(indexType), name, null)
        index.table = table ?: this.table?.toBO()
        val columnList = ArrayList<ColumnInIndex>()
        this.columnList?.forEach { columnList.add(it.toBO(index)) }
        index.columnList = columnList
        return index
    }
}
