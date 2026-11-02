package com.rohith.pocket.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rohith.pocket.ui.theme.PocketGreen
import com.rohith.pocket.ui.theme.TextPrimary
import com.rohith.pocket.ui.theme.TextSecondary

@Composable
fun SettingsScreen(onBack: () -> Unit, isNotificationAccessGranted: Boolean) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text("Settings", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            icon = Icons.Default.Notifications,
            title = "Notification Access",
            subtitle = if (isNotificationAccessGranted) "Connected" else "Not connected — tap to enable",
            trailing = {
                if (isNotificationAccessGranted) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PocketGreen)
                }
            },
            onClick = { context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        )
        SettingsRow(icon = Icons.Default.Category, title = "Categories", subtitle = "Manage your categories")
        SettingsRow(icon = Icons.Default.CurrencyRupee, title = "Currency", subtitle = "INR (₹)")
        SettingsRow(icon = Icons.Default.CloudUpload, title = "Data & Backup", subtitle = "Local only — no cloud sync yet")
        SettingsRow(icon = Icons.Default.Palette, title = "Appearance", subtitle = "System default")
        SettingsRow(icon = Icons.Default.Info, title = "About", subtitle = "Pocket v1.0.0")
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = TextSecondary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
        trailing?.invoke() ?: Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
    }
    Divider(color = com.rohith.pocket.ui.theme.Divider)
}
