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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.home.tracker.AppViewModelProvider
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.ui.SummaryType
import org.home.tracker.ui.common.AggregationsChart
import org.home.tracker.ui.common.DateSwitchPanel
import org.home.tracker.ui.expense.component.ExpenseItem
import org.home.tracker.ui.summary.viewmodel.SummaryViewModel
import org.home.tracker.util.Constants.DateTime.CURRENT_YEAR
import java.time.LocalDate

@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    type: SummaryType
) {

    val items by remember { viewModel.items }
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
                val dto = ExpenseDto(
                    id = 0L,
                    value = item.value,
                    category = CategoryDto(null, item.category),
                    currencyId = item.currency
                )
                ExpenseItem(dto) {}
            }
        }
    }
}