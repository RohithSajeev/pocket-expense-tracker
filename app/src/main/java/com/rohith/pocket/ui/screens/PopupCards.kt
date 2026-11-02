package com.rohith.pocket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rohith.pocket.data.Category
import com.rohith.pocket.ui.components.categoryColor
import com.rohith.pocket.ui.components.categoryIcon
import com.rohith.pocket.ui.components.formatAmount
import com.rohith.pocket.ui.theme.*

@Composable
fun CategoryPopupCard(
    amount: Double,
    payee: String,
    onCategoryChosen: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(28.dp).background(PocketGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("P", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pocket", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(56.dp)
                        .background(BackgroundLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Store, contentDescription = null, tint = TextSecondary)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "You paid",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "₹${formatAmount(amount)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "to $payee",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text("Choose a category", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(10.dp))

                val categories = Category.entries.toList()
                categories.chunked(3).forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowCategories.forEach { category ->
                            CategoryChoiceButton(
                                category = category,
                                modifier = Modifier.weight(1f),
                                onClick = { onCategoryChosen(category) }
                            )
                        }
                        // Pad the row if fewer than 3 items so widths stay even.
                        repeat(3 - rowCategories.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Maybe later", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun CategoryChoiceButton(category: Category, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val color = categoryColor(category)
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.12f),
            contentColor = color
        ),
        contentPadding = PaddingValues(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(categoryIcon(category), contentDescription = category.label)
            Spacer(modifier = Modifier.height(2.dp))
            Text(category.label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun SavedConfirmationCard(amount: Double, category: Category, onDone: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
        ) {
            Column(
                modifier = Modifier.padding(28.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(64.dp).background(SuccessGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = SurfaceWhite)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text("Saved!", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "₹${formatAmount(amount)} added to ${category.label}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PocketGreen)
                ) {
                    Text("Done", color = SurfaceWhite)
                }
            }
        }
    }
}
