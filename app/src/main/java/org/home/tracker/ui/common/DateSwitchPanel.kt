package org.home.tracker.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.home.tracker.ui.SummaryType
import org.home.tracker.ui.expense.stringify
import org.home.tracker.util.toMillis
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSwitchPanel(
    startDate: LocalDate,
    onDateChange: (LocalDate) -> Unit = {},
    stepType: SummaryType = SummaryType.MONTH_BY_MONTH
) {

    var currentDate by remember { mutableStateOf(LocalDate.of(startDate.year, startDate.month, 1)) }

    val selectedDate = rememberDatePickerState(
        initialSelectedDateMillis = currentDate.toMillis()
    )

    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                currentDate = currentDate.minus(1L, ChronoUnit.MONTHS)
                selectedDate.setSelection(currentDate.toMillis())
                onDateChange(currentDate)
            }
        ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = stringify(selectedDate.selectedDateMillis ?: 0L),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .weight(3f)
//                .clickable { showDatePicker = true }
        )
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                currentDate = currentDate.plus(1L, ChronoUnit.MONTHS)
                selectedDate.setSelection(currentDate.toMillis())
                onDateChange(currentDate)
            }
        ) {
            Icon(Icons.Rounded.ArrowForward, contentDescription = "Forward")
        }
    }
}