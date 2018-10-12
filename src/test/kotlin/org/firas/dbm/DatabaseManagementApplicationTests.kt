package org.firas.dbm.databasemanagement

import org.firas.dbm.dao.DatabaseDAO
import org.firas.dbm.po.DatabasePO
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import javax.annotation.Resource

@RunWith(SpringRunner::class)
@SpringBootTest
class DatabaseManagementApplicationTests {

	@Test
	fun contextLoads() {
		/*
		databaseDAO!!.saveAll(Arrays.asList(
				DatabasePO("bsp_dev", "oracle", "bsp", "{}",
						null, "218.168.127.193", 1521),
				DatabasePO("bsp_test", "oracle", "bsp", "{}",
						null, "218.168.127.146", 1521)
		))
		*/
	}

	@Resource
	var databaseDAO: DatabaseDAO? = null
}
