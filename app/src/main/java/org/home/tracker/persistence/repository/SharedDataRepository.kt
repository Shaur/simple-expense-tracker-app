package org.home.tracker.persistence.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SharedDataRepository {

    private val updateTime = MutableStateFlow(System.currentTimeMillis())

    val updateTimeFlow = updateTime.asStateFlow()

    fun updateTime() {
        updateTime.value = System.currentTimeMillis()
    }

}