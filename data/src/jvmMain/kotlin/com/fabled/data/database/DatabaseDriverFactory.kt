package com.fabled.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.fabled.data.FabledDatabase
import java.io.File

object DatabaseDriverFactory {
    fun create(databasePath: String = "fabled.db"): SqlDriver {
        val dbFile = File(databasePath)
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        FabledDatabase.Schema.create(driver)
        return driver
    }
}
