package com.fabled.shared.util

import kotlin.random.Random

object IdGenerator {
    fun generate(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return buildString {
            repeat(8) { append(chars[Random.nextInt(chars.length)]) }
            append('-')
            repeat(4) { append(chars[Random.nextInt(chars.length)]) }
            append('-')
            repeat(4) { append(chars[Random.nextInt(chars.length)]) }
            append('-')
            repeat(12) { append(chars[Random.nextInt(chars.length)]) }
        }
    }
}
