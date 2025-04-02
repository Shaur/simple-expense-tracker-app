package org.home.tracker.ui.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.home.tracker.AppViewModelProvider
import org.home.tracker.ui.SummaryType
import org.home.tracker.ui.common.AggregationsChart
import org.home.tracker.ui.common.DateSwitchPanel
import org.home.tracker.ui.expense.component.ExpenseItem
import org.home.tracker.ui.expense.component.SummaryItem
import org.home.tracker.ui.summary.viewmodel.SummaryViewModel
import org.home.tracker.util.Constants.DateTime.CURRENT_YEAR
import java.time.LocalDate

@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    type: SummaryType
) {

    val items by remember { viewModel.items }
    val subItems by remember { viewModel.subItems }
    val aggregations by remember { viewModel.aggregations }
    var date by remember {
        mutableStateOf(LocalDate.of(CURRENT_YEAR, LocalDate.now().month, 1))
    }

    viewModel.initAggregations(type)
    viewModel.initItems(date, type)

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .padding(paddings)
                .verticalScroll(rememberScrollState())
        ) {

            AggregationsChart(aggregations = aggregations)
            DateSwitchPanel(date, onDateChange = { date = it })
            for (item in items) {
                SummaryItem(item) {
                    if (subItems[item.categoryId].isNullOrEmpty()) {
                        viewModel.initSubitems(date, type, item.categoryId)
                    } else {
                        viewModel.clearSubItems(item.categoryId)
                    }
                }

                Column(modifier = Modifier.padding(start = 10.dp)) {
                    if (!subItems[item.categoryId].isNullOrEmpty()) {
                        for (sub in subItems.getValue(item.categoryId)) {
                            ExpenseItem(sub) { }
                        }
                    }
                }
            }
        }
    }
}