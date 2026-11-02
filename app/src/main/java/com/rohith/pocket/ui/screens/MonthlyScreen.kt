package com.rohith.pocket.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rohith.pocket.ui.components.PocketCard
import com.rohith.pocket.ui.components.categoryColor
import com.rohith.pocket.ui.components.formatAmount
import com.rohith.pocket.ui.theme.PocketGreen
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.ui.theme.TextSecondary
import com.rohith.pocket.viewmodel.TransactionViewModel

@Composable
fun MonthlyScreen(viewModel: TransactionViewModel, onBack: () -> Unit) {
    val monthlyTotal by viewModel.monthlyTotal.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val grandTotal = categoryTotals.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text("Monthly Spending", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous month")
            Text("This Month", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            Icon(Icons.Default.ChevronRight, contentDescription = "Next month")
        }

        Text(
            "₹${formatAmount(monthlyTotal)}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Simple bar chart placeholder for last 6 months — wire this up to a
        // real per-month query once you're past the demo stage.
        PocketCard {
            Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                val barWidth = size.width / 8
                val heights = listOf(0.3f, 0.5f, 0.4f, 0.6f, 0.55f, 1f)
                heights.forEachIndexed { index, fraction ->
                    val barHeight = size.height * fraction
                    drawRect(
                        color = if (index == heights.lastIndex) PocketGreen else PocketGreen.copy(alpha = 0.25f),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            x = barWidth * (index + 1),
                            y = size.height - barHeight
                        ),
                        size = Size(barWidth * 0.6f, barHeight)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Top Categories", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        categoryTotals.sortedByDescending { it.total }.take(3).forEach { total ->
            PocketCard(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(total.category.label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("₹${formatAmount(total.total)}", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "${((total.total / grandTotal) * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = categoryColor(total.category)
                        )
                    }
                }
            }
        }
    }
}
