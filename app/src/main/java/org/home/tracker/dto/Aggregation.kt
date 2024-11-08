package org.home.tracker.dto

abstract class Aggregation(
    open val currency: String,
    open val value: Long
) {

    abstract fun getTimeAxis(): String

    abstract fun getTimeAxisId(): Float

}