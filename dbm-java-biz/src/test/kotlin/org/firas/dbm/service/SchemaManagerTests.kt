package org.firas.dbm.service

import org.firas.dbm.databasemanagement.DatabaseManagementApplication
import org.firas.dbm.dto.SchemaDTO
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.annotation.Resource
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [DatabaseManagementApplication::class])
class SchemaManagerTests {

	companion object {
	    private val log = LoggerFactory.getLogger(SchemaManagerTests::class.java)
	}

	var databaseId = ""

	@Before
	fun setUp() {
		val properties = Properties()
        val inputStream = this.javaClass.getResourceAsStream("/test-param.properties")
		properties.load(inputStream)
		this.databaseId = properties.getProperty("SchemaManagerTests.databaseId") ?: ""
	}

	@Test
    @Transactional
	fun contextLoads() {
		val list = schemaManager!!.findByDatabase(this.databaseId)
        log.info("${list.size}")

		val dto = schemaManager!!.create(
				SchemaDTO(null, "test", this.databaseId))
        log.info("${dto.recId}")

        val list1 = schemaManager!!.findByDatabase(this.databaseId)
        Assert.assertEquals(list.size + 1, list1.size)

		val newDTO = SchemaDTO(dto.recId, "TestTest", dto.databaseId)
        schemaManager!!.update(newDTO)

		schemaManager!!.findByDatabase(this.databaseId).forEach{
			if (dto.recId == it.recId) {
				Assert.assertEquals("TestTest", it.name)
			}
		}
	}

	@Resource
	var schemaManager: SchemaManager? = null
}
