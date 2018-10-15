package org.firas.dbm.po

import org.firas.common.po.PoBase
import org.firas.dbm.bo.Index
import org.firas.dbm.bo.IndexType
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

    override fun toDTO(): IndexDTO {
        return IndexDTO(IndexType.values().get(indexType), name, table?.recId)
    }

    override fun toBO(): Index {
        val table = this.table?.toBO()
        val columnList = this.columnList?.stream()
                ?.map { it.column!!.toBO(table) }
                ?.collect(Collectors.toList())
        return Index(IndexType.values().get(indexType), name,
                columnList ?: ArrayList(1))
    }
}