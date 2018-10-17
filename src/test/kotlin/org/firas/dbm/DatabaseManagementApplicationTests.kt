package org.firas.dbm.databasemanagement

import org.firas.dbm.dao.ColumnDAO
import org.firas.dbm.dao.DatabaseDAO
import org.firas.dbm.dao.SchemaDAO
import org.firas.dbm.dao.TableDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class DatabaseManagementApplicationTests {

	companion object {
	    private val log = LoggerFactory.getLogger(DatabaseManagementApplicationTests::class.java)
	}

	@Test
    @Transactional
	fun contextLoads() {
		val table = tableDAO!!.findOne {
			root, query, criteriaBuilder ->
			criteriaBuilder.equal(root.get<String>("name"), "SETTSC_PD_DTL")
		}.get()
        log.info("%d".format(table.toBO().columnMap.size))
	}

	@Resource
	var databaseDAO: DatabaseDAO? = null

	@Resource
	var schemaDAO: SchemaDAO? = null

	@Resource
	var tableDAO: TableDAO? = null

	@Resource
	var columnDAO: ColumnDAO? = null
}
