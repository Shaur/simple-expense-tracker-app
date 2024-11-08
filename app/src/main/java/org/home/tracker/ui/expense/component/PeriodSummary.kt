package org.home.tracker.ui.expense.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.home.tracker.ui.SummaryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSummary(
    title: String,
    values: Map<String, Long>,
    navigateToSummary: () -> Unit = {}
) {

    val options = values.entries.sortedByDescending { it.value }.take(2)

    Card(
        onClick = { navigateToSummary() },
        modifier = Modifier
            .size(120.dp, 100.dp)
            .padding(10.dp)
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.padding(start = 5.dp, top = 5.dp)) {
            for (option in options) {
                val value = option.value / 100f
                Text(text = String.format("%.2f ${option.key}", value))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PeriodSummaryPreview() {
    val yesterday = mapOf("RUB" to 10000000L)
    val today = mapOf("RUB" to 10000L, "KGS" to 9000L)

    MaterialTheme {
        Row {
            PeriodSummary(title = "Yesterday", values = yesterday)
            PeriodSummary(title = "Today", values = today)
        }
    }
}