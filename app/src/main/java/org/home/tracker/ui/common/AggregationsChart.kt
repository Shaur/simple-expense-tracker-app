package org.home.tracker.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import com.patrykandpatrick.vico.compose.common.vicoTheme
import org.home.tracker.dto.Aggregation
import org.home.tracker.dto.MonthlyExpenseDto
import kotlin.collections.listOf

@Composable
fun AggregationsChart(aggregations: Map<String, List<Aggregation>>) {

    if (aggregations.isEmpty()) return

    val legendItemLabelComponent = rememberTextComponent(TextStyle(vicoTheme.textColor))

    val modelProducer = remember { CartesianChartModelProducer() }

    val labelMapKey = ExtraStore.Key<Map<Float, String>>()

    val data = aggregations.values.flatten().associate { it.getTimeAxisId() to it.getTimeAxis() }

    val x = aggregations.values.flatten().map { it.getTimeAxisId() }.toSet().sorted()
    
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnModel {
                aggregations.keys.forEach { currency ->
                    val values = aggregations.getValue(currency)
                    series(x = x, y = values.map { it.value / 100f })
                }
            }
            extras { it[labelMapKey] = data }
        }
    }

    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[labelMapKey].getValue(x.toFloat())
    }

    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            marker = rememberDefaultCartesianMarker(legendItemLabelComponent),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() }
            )
        ),
        modelProducer = modelProducer
    )
}

@Composable
@Preview
fun AggregationsChartPreview() {
    val aggregations = mapOf(
        "USD" to listOf(
            MonthlyExpenseDto(7, 2026, "USD", 100000),
            MonthlyExpenseDto(8, 2026, "USD", 50000),
        ),
        "EUR" to listOf(MonthlyExpenseDto(7, 2026, "EUR", 20000))
    )
    Box(modifier = Modifier
        .background(Color.White)
        .padding(16.dp)
    ) {
        AggregationsChart(aggregations)
    }
}