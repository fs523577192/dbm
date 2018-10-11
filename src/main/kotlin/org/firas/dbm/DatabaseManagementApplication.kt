package org.firas.dbm.databasemanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:dbm-context.xml")
class DatabaseManagementApplication

fun main(args: Array<String>) {
    runApplication<DatabaseManagementApplication>(*args)
}
