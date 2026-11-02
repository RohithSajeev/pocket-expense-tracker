package com.rohith.pocket.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rohith.pocket.data.Category
import com.rohith.pocket.ui.components.*
import com.rohith.pocket.ui.theme.PocketGreen
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.ui.theme.TextSecondary
import com.rohith.pocket.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(viewModel: TransactionViewModel, onOpenSettings: () -> Unit, onSeeAllHistory: () -> Unit) {
    val transactions by viewModel.transactions.collectAsState()
    val monthlyTotal by viewModel.monthlyTotal.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()

    val slices = categoryTotals.map {
        DonutSlice(it.category.label, it.total, categoryColor(it.category))
    }
    val grandTotal = categoryTotals.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pocket", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary)
                }
            }
        }

        item {
            PocketCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Spent", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                    Text("This Month", style = MaterialTheme.typography.bodyMedium, color = PocketGreen)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "₹${formatAmount(monthlyTotal)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = PocketGreen, modifier = Modifier.size(14.dp))
                    Text(" vs last month", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }

        item {
            PocketCard {
                Text("Spending by Category", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
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
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recent Transactions", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(
                    "See all",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PocketGreen,
                    modifier = Modifier.clickable(onClick = onSeeAllHistory)
                )
            }
        }

        items(transactions.take(5)) { transaction ->
            PocketCard { TransactionRow(transaction) }
        }
    }
}
