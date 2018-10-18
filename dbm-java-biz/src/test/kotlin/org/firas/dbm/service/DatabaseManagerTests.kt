package org.firas.dbm.databasemanagement

import org.firas.dbm.dialect.MySQLDialect
import org.firas.dbm.dto.DatabaseDTO
import org.firas.dbm.service.DatabaseManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class DatabaseManagerTests {

	companion object {
	    private val log = LoggerFactory.getLogger(DatabaseManagerTests::class.java)
	}

	@Test
    @Transactional
	fun contextLoads() {
		val list = databaseManager!!.listAll()
        log.info("${list.size}")

		val dto = databaseManager!!.create(
				DatabaseDTO(null, MySQLDialect.instance, "usersc"))
        log.info("${dto.recId}")

        val list1 = databaseManager!!.listAll()
        Assert.assertEquals(list.size + 1, list1.size)

		dto.host = "218.168.127.193"
		dto.port = 3306
        databaseManager!!.update(dto)

		databaseManager!!.listAll().forEach{
			if ("usersc" == it.name) {
				Assert.assertEquals("218.168.127.193", it.host)
				Assert.assertEquals(3306, it.port)
			}
		}
	}

	@Resource
	var databaseManager: DatabaseManager? = null
}
