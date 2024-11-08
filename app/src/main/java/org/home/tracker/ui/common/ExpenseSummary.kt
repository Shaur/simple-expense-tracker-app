package org.home.tracker.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.home.tracker.ui.SummaryType
import org.home.tracker.util.toMillis
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseSummary(
    from: LocalDate,
    summary: Map<String, Long>,
    onClick: (SummaryType) -> Unit,
    onDateChange: (Long) -> Unit
) {
    val toString = from.plusMonths(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)
    val fromString = from.format(DateTimeFormatter.ISO_LOCAL_DATE)
    val selectedDate = rememberDatePickerState(initialSelectedDateMillis = from.toMillis())
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateChange(selectedDate.selectedDateMillis ?: 0L)
                        showDatePicker = false
                    }
                ) {
                    androidx.compose.material3.Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) { androidx.compose.material3.Text("Dismiss") }
            }
        ) {
            DatePicker(state = selectedDate)
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    text = "$fromString - $toString",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(10f)
                        .clickable { showDatePicker = true }
                )
                IconButton(
                    onClick = {
                        onClick(SummaryType.MONTH_BY_MONTH)
                    },
                    modifier = Modifier
                        .height(25.dp)
                        .width(25.dp)
                        .weight(1f)
                ) {
                    Icon(Icons.Rounded.Info, contentDescription = null)
                }

            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(start = 5.dp)
            ) {
                items(summary.entries.toList()) { entry ->
                    val total = entry.value / 100f
                    Text(text = String.format("%.2f ${entry.key}", total))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    val state = mapOf("KGS" to 1000L)
    MaterialTheme {
        ExpenseSummary(LocalDate.of(2023, 1, 1), state, onDateChange = {}, onClick = { })
    }
}