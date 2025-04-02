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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import org.home.tracker.dto.ExtendedSummaryDto
import java.util.Locale

@Composable
fun SummaryItem(info: ExtendedSummaryDto, onClick: (ExtendedSummaryDto) -> Unit) {
    val value = String.format(Locale.US, "%.2f %s", info.value / 100f, info.currency)
    val prevValue = String.format(Locale.US, "%.2f %s", info.prevValue / 100f, info.currency)

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
                text = info.category,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 30.dp)
                    .align(Alignment.CenterVertically)
            )

            Column(modifier = Modifier.padding(top = 5.dp, end = 40.dp)) {

                if (info.prevValue == 0L) {
                    Row(modifier = Modifier
                        .weight(3f)
                        .fillMaxHeight()) {
                        Text(
                            text = value,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        ComparationIcon(
                            info.value,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 4.dp)
                        )
                    }
                }

                if (info.prevValue != 0L) {
                    Row(modifier = Modifier.weight(3f)) {
                        Text(
                            text = value,
                            fontWeight = FontWeight.Medium
                        )
                        ComparationIcon(info.value, prevValue = info.prevValue)
                    }

                    Text(
                        text = prevValue,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Right,
                        color = Color.Gray,
                        modifier = Modifier.weight(3f)
                    )
                }
            }
        }
    }
}

@Composable
fun ComparationIcon(value: Long, modifier: Modifier = Modifier, prevValue: Long = 0L) {
    if (value == prevValue || prevValue == 0L) {
        Icon(Icons.Filled.Info, contentDescription = "Undefined", modifier = modifier)
    } else if (value > prevValue) {
        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up", modifier = modifier)
    } else {
        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Down", modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSummaryItem() {
    val dto = ExtendedSummaryDto(
        value = 56750L,
        prevValue = 5647L,
        category = "Продукты",
        categoryId = 1L,
        currency = "KGS"
    )

    MaterialTheme {
        Column {
            SummaryItem(dto) {}
            SummaryItem(dto.copy(prevValue = 0L)) { }
        }
    }
}