package com.rohith.pocket.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.ui.theme.TextSecondary

data class DonutSlice(val label: String, val amount: Double, val color: Color)

@Composable
fun DonutChart(
    slices: List<DonutSlice>,
    centerLabel: String,
    centerValue: String,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 160.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 22.dp
) {
    val total = slices.sumOf { it.amount }.takeIf { it > 0 } ?: 1.0

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            var startAngle = -90f
            slices.forEach { slice ->
                val sweep = (slice.amount / total * 360f).toFloat()
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweep.coerceAtLeast(0.5f),
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt),
                    size = Size(size.toPx() - strokeWidth.toPx(), size.toPx() - strokeWidth.toPx()),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        strokeWidth.toPx() / 2,
                        strokeWidth.toPx() / 2
                    )
                )
                startAngle += sweep
            }
        }
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(centerValue, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Text(centerLabel, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}
