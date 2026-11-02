package com.rohith.pocket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rohith.pocket.data.Category
import com.rohith.pocket.data.Transaction
import com.rohith.pocket.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

fun categoryColor(category: Category): Color = when (category) {
    Category.FOOD -> CategoryFood
    Category.GROCERIES -> CategoryGroceries
    Category.TRAVEL -> CategoryTravel
    Category.PETROL -> CategoryPetrol
    Category.SHOPPING -> CategoryShopping
    Category.OTHERS -> CategoryOthers
}

fun categoryIcon(category: Category): ImageVector = when (category) {
    Category.FOOD -> Icons.Default.Restaurant
    Category.GROCERIES -> Icons.Default.ShoppingCart
    Category.TRAVEL -> Icons.Default.DirectionsCar
    Category.PETROL -> Icons.Default.LocalGasStation
    Category.SHOPPING -> Icons.Default.ShoppingBag
    Category.OTHERS -> Icons.Default.MoreHoriz
}

@Composable
fun LegendRow(label: String, amount: Double, percent: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(10.dp).background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, modifier = Modifier.weight(1f))
        Text("$percent%", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(categoryColor(transaction.category).copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = categoryIcon(transaction.category),
                contentDescription = transaction.category.label,
                tint = categoryColor(transaction.category)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.payee, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Text(
                "${transaction.category.label} • ${formatTime(transaction.timestamp)}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
        Text(
            "- ₹${formatAmount(transaction.amount)}",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PocketCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp), content = content)
    }
}

fun formatAmount(amount: Double): String {
    return if (amount % 1.0 == 0.0) "%,d".format(amount.toLong())
    else "%,.2f".format(amount)
}

fun formatTime(timestampMillis: Long): String {
    val sdf = SimpleDateFormat("d MMM, h:mm a", Locale.getDefault())
    return sdf.format(Date(timestampMillis))
}
