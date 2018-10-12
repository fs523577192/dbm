package org.firas.dbm.databasemanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportResource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ImportResource("classpath:dbm-context.xml")
@EnableJpaRepositories(basePackages = ["org.firas.dbm"])
class DatabaseManagementApplication

fun main(args: Array<String>) {
    runApplication<DatabaseManagementApplication>(*args)
}
