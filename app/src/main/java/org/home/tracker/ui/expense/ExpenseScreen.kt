package org.home.tracker.ui.expense

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.home.tracker.AppViewModelProvider
import org.home.tracker.dto.ExpenseDto
import org.home.tracker.ui.SummaryType
import org.home.tracker.ui.common.AddExpenseDialog
import org.home.tracker.ui.common.ExpenseSummary
import org.home.tracker.ui.expense.component.ExpenseItem
import org.home.tracker.ui.expense.component.PeriodSummary
import org.home.tracker.ui.expense.viewmodel.ExpenseViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToSummaryScreen: (type: SummaryType) -> Unit
) {
    val today = LocalDate.now()

    var showAddMenu by remember { mutableStateOf(false) }
    val beginPeriodState by remember { viewModel.beginPeriod }

    val periodicSummary by remember { viewModel.periodSummaryState }

    val items by remember { viewModel.state }
    val categories by remember { viewModel.categories }
    var selectedItem by remember { mutableStateOf<ExpenseDto?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddMenu = true },
                shape = CircleShape,
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
            ) { Icon(Icons.Filled.Add, "Added new expense") }
        },
    ) { paddings ->

        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            modifier = Modifier
                .padding(paddings)
                .fillMaxHeight()
        ) {
            item {
                ExpenseSummary(
                    beginPeriodState,
                    periodicSummary.month,
                    navigateToSummaryScreen,
                    onDateChange = { viewModel.updateBeginPeriod(it) }
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    PeriodSummary(title = "Yesterday", values = periodicSummary.yesterday)
                    PeriodSummary(title = "Today", values = periodicSummary.today)
                    PeriodSummary(
                        title = "Week",
                        values = periodicSummary.week,
                        navigateToSummary = { navigateToSummaryScreen(SummaryType.WEEK_BY_WEEK) }
                    )
                }
            }

            item {
                Text(
                    text = "Today",
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 7.dp, start = 10.dp)
                )
            }
            items(items.filter { it.getLocalDate() == today }) {
                ExpenseItem(info = it) { expense ->
                    selectedItem = expense
                    showAddMenu = true
                }
            }

            item {
                Text(
                    text = "Later",
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 7.dp, start = 10.dp)
                )
            }
            items(items.filter { it.getLocalDate() != today }) {
                ExpenseItem(info = it) { expense ->
                    selectedItem = expense
                    showAddMenu = true
                }
            }
        }
    }

    if (showAddMenu) {
        ModalBottomSheet(
            onDismissRequest = {
                selectedItem = null
                showAddMenu = false
            },
            sheetState = rememberModalBottomSheetState()
        ) {
            AddExpenseDialog(expense = selectedItem, categories = categories) { expense ->
                viewModel.save(expense)
                selectedItem = null
                showAddMenu = false
            }
        }
    }

}

fun stringify(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE)
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenPreview() {
    MaterialTheme {
        ExpenseScreen {}
    }
}