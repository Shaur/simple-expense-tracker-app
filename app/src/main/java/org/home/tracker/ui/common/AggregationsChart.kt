package org.home.tracker.ui.common

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.cornered.CorneredShape
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.home.tracker.dto.Aggregation

@Composable
fun AggregationsChart(aggregations: Map<String, MutableList<Aggregation>>) {

    if (aggregations.isEmpty()) return

    val entries = aggregations.values.map { agg ->
        entriesOf(*agg.map { it.getTimeAxisId() to it.value / 100f }.toTypedArray())
    }

    val chartEntryModel = entryModelOf(*entries.toTypedArray())

    val colors = List(aggregations.keys.size) { index ->
        Color.argb(1f, index / 1f, index / 2f, index / 3f)
    }

    val legendItems = aggregations.keys.mapIndexed { index, currency ->
        legendItem(
            icon = lineComponent(thickness = 2.dp, shape = CorneredShape()),
            label = textComponent { this.color = colors[index] },
            labelText = currency
        )
    }

    val legend = verticalLegend(
        items = legendItems,
        iconSize = 10.dp,
        iconPadding = 5.dp
    )

    val values = aggregations.values.flatten().toSet()
    val timeAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        values.find { it.getTimeAxisId() == value }?.getTimeAxis() ?: ""
    }

    ProvideChartStyle(
        chartStyle = currentChartStyle.copy(
            columnChart = ChartStyle.ColumnChart(colors.map { LineComponent(it) })
        )
    ) {
        Chart(
            chart = columnChart(),
            model = chartEntryModel,
            legend = legend,
            marker = MarkerComponent(textComponent { }, null, null),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(valueFormatter = timeAxisValueFormatter),
            modifier = Modifier.background(androidx.compose.ui.graphics.Color.DarkGray)
        )
    }

}