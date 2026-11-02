package com.rohith.pocket.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rohith.pocket.data.Category
import com.rohith.pocket.ui.components.PocketCard
import com.rohith.pocket.ui.components.TransactionRow
import com.rohith.pocket.ui.theme.PocketGreen
import com.rohith.pocket.ui.theme.SurfaceWhite
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.viewmodel.TransactionViewModel

@Composable
fun HistoryScreen(viewModel: TransactionViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val filtered = if (selectedCategory == null) transactions
    else transactions.filter { it.category == selectedCategory }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Transactions", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PocketGreen, selectedLabelColor = SurfaceWhite)
                )
            }
            items(Category.entries.toList()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.label) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PocketGreen, selectedLabelColor = SurfaceWhite)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered) { transaction ->
                PocketCard { TransactionRow(transaction) }
            }
        }
    }
}
