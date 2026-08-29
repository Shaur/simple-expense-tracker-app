package org.home.tracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.home.tracker.service.BankNotificationListenerService
import org.home.tracker.ui.navigation.NavigationRoutes
import org.home.tracker.ui.navigation.graph

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNotificationListenerPermissionGranted(this)) {
            redirectToSettings(this)
        }

        setContent {
            MaterialTheme {
                MainApp()
            }
        }

    }

    fun redirectToSettings(activity: AppCompatActivity) {
        Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
            activity.startActivityForResult(this, 1001)
        }
    }

    fun isNotificationListenerPermissionGranted(context: Context): Boolean {
        val componentName = ComponentName(context, BankNotificationListenerService::class.java)
        val enabledListeners = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return enabledListeners?.contains(componentName.flattenToString()) ?: false
    }

    @Composable
    fun MainApp() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainAppNavHost()
        }
    }

    @Composable
    fun MainAppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = NavigationRoutes.Graph.Root.route
        ) {
            graph(controller = navController)
        }
    }

}