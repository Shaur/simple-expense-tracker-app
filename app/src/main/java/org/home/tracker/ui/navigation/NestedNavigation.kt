package org.home.tracker.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.patrykandpatrick.vico.core.extension.getFieldValue
import org.home.tracker.ui.SummaryType
import org.home.tracker.ui.expense.ExpenseScreen
import org.home.tracker.ui.summary.SummaryScreen

fun NavGraphBuilder.graph(controller: NavController) {
    navigation(
        route = NavigationRoutes.Graph.Root.route,
        startDestination = NavigationRoutes.Graph.Expense.route
    ) {
        composable(route = NavigationRoutes.Graph.Expense.route) {
            ExpenseScreen {
                controller.navigate(route = NavigationRoutes.Graph.Summary.route + "/$it")
            }
        }

        composable(
            route = NavigationRoutes.Graph.Summary.route + "/{type}",
            arguments = listOf(
                navArgument("type") { type = NavType.EnumType(SummaryType::class.java); nullable = false}
            )
        ) {
            val type: SummaryType = it.arguments?.getSerializable("type", SummaryType::class.java)!!
            SummaryScreen(type = type)
        }
    }
}