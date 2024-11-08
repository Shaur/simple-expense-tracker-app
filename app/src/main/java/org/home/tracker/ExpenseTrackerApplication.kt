package org.home.tracker

import android.app.Application
import org.home.tracker.persistence.AppContainer
import org.home.tracker.persistence.AppDataContainer

class ExpenseTrackerApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppDataContainer(this)
    }
}