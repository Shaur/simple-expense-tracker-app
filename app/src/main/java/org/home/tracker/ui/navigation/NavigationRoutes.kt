package org.home.tracker.ui.navigation

sealed class NavigationRoutes {

    sealed class Graph(val route: String): NavigationRoutes() {

        data object Root : Graph(route = "root")

        data object Expense : Graph(route = "expense")

        data object Summary : Graph(route = "summary")

    }

}