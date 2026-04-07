package com.fabled.data.database

import com.fabled.data.FabledDatabase

fun createFabledDatabase(databasePath: String = "fabled.db"): FabledDatabase {
    val driver = DatabaseDriverFactory.create(databasePath)
    return FabledDatabase(driver)
}
