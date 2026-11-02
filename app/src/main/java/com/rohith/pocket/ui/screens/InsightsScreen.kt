package com.rohith.pocket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rohith.pocket.ui.components.*
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.ui.theme.TextSecondary
import com.rohith.pocket.viewmodel.TransactionViewModel

@Composable
fun InsightsScreen(viewModel: TransactionViewModel) {
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val monthlyTotal by viewModel.monthlyTotal.collectAsState()
    val grandTotal = categoryTotals.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

    val slices = categoryTotals.map { DonutSlice(it.category.label, it.total, categoryColor(it.category)) }
    val topCategory = categoryTotals.maxByOrNull { it.total }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Insights", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))

        PocketCard {
            Text("Category Breakdown", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DonutChart(
                    slices = slices,
                    centerLabel = "This Month",
                    centerValue = "₹${formatAmount(monthlyTotal)}"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    categoryTotals.forEach {
                        LegendRow(
                            label = it.category.label,
                            amount = it.total,
                            percent = ((it.total / grandTotal) * 100).toInt(),
                            color = categoryColor(it.category)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (topCategory != null) {
            PocketCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(categoryIcon(topCategory.category), contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            "${topCategory.category.label} is your highest expense",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            "You've spent ${((topCategory.total / grandTotal) * 100).toInt()}% (₹${formatAmount(topCategory.total)}) on this, this month.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
