package com.rohith.pocket

import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.rohith.pocket.navigation.PocketNavGraph
import com.rohith.pocket.ui.theme.PocketTheme
import com.rohith.pocket.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModel.Factory((application as PocketApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketTheme {
                PocketNavGraph(
                    viewModel = viewModel,
                    isNotificationAccessGranted = isNotificationAccessGranted()
                )
            }
        }
    }

    private fun isNotificationAccessGranted(): Boolean {
        val enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return enabledListeners?.contains(packageName) == true
    }
}
