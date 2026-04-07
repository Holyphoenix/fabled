package com.fabled.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.fabled.data.FabledDatabase
import java.io.File

object DatabaseDriverFactory {
    fun create(databaseName: String = "fabled.db"): SqlDriver {
        // Store the database in a stable, writable user-specific directory.
        val dataDir = File(System.getProperty("user.home"), ".fabled").also { it.mkdirs() }
        val dbFile = File(dataDir, databaseName)
        val isNewDatabase = !dbFile.exists()

        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        // Only create the schema on a fresh database; calling Schema.create() on an
        // existing database throws "table already exists" and crashes the app.
        if (isNewDatabase) {
            FabledDatabase.Schema.create(driver)
        }

        return driver
    }
}

