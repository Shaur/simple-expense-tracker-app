package org.home.tracker.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDate.toMillis(): Long {
    return this.toEpochDay() * 24 * 60 * 60 * 1000
}

fun LocalDateTime.toMillis(): Long {
    return this.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()
}