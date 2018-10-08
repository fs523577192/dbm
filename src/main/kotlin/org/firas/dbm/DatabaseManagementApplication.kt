package org.firas.dbm.databasemanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DatabaseManagementApplication

fun main(args: Array<String>) {
    runApplication<DatabaseManagementApplication>(*args)
}
