package org.home.tracker.ui.expense.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.home.tracker.dto.CategoryDto
import org.home.tracker.dto.ExpenseDto
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseItem(info: ExpenseDto, onClick: (ExpenseDto) -> Unit) {
    val stringifyDate = info.date?.let {
        Instant.ofEpochMilli(info.date)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
    }


    val value = String.format("%.2f %s", info.value / 100f, info.currencyId)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 7.dp, start = 10.dp, end = 10.dp)
            .border(BorderStroke(1.dp, Color.Transparent))
            .clickable { onClick(info) }
    ) {
        Row(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = info.category.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 30.dp)
                    .align(Alignment.CenterVertically)
            )

            if (stringifyDate == null) {
                Text(
                    text = value,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .weight(3f)
                        .padding(end = 40.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            if (stringifyDate != null) {
                Column(modifier = Modifier.padding(top = 5.dp, end = 40.dp)) {
                    Text(
                        text = value,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(3f)
                    )

                    Text(
                        stringifyDate,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.weight(3f)
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewExpenseItem() {
    val dto = ExpenseDto(
        id = 0,
        date = System.currentTimeMillis(),
        value = 56750L,
        category = CategoryDto(1, "Продукты"),
        currencyId = "KGS"
    )

    MaterialTheme {
        ExpenseItem(dto) {}
    }
}