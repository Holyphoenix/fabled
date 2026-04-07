package com.fabled.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.fabled.app.di.appModule
import com.fabled.app.ui.App
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Fabled — Novel Writing Studio",
            state = rememberWindowState(width = 1400.dp, height = 900.dp)
        ) {
            App()
        }
    }
}

