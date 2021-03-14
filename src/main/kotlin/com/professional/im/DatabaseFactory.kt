package com.professional.im

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
/**
 * @author : bgubanov
 * @since : 14.03.2021
 **/

object DatabaseFactory {
    fun init(){
        Database.connect(hikariDS)
        transaction {
            create(Notes)
        }
    }

    private val hikariDS by lazy {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        HikariDataSource(config)
    }
}