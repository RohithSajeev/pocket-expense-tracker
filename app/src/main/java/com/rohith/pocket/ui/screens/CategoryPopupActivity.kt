package com.rohith.pocket.ui.screens

import android.app.KeyguardManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rohith.pocket.PocketApplication
import com.rohith.pocket.data.Category
import com.rohith.pocket.ui.theme.PocketTheme
import com.rohith.pocket.viewmodel.TransactionViewModel

/**
 * A transparent, always-on-top Activity. It is launched by the full-screen
 * intent in PaymentNotificationListener the instant a GPay payment is
 * detected, so it appears immediately — even over the lock screen — asking
 * the user to pick a category (the "3. Category Popup" screen in the design).
 */
class CategoryPopupActivity : ComponentActivity() {

    companion object {
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_PAYEE = "extra_payee"
        const val EXTRA_TIMESTAMP = "extra_timestamp"
    }

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModel.Factory((application as PocketApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showOverLockScreen()

        val amount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)
        val payee = intent.getStringExtra(EXTRA_PAYEE) ?: "Unknown"
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, System.currentTimeMillis())

        setContent {
            PocketTheme {
                var saved by remember { mutableStateOf(false) }
                var chosenCategory by remember { mutableStateOf<Category?>(null) }

                if (!saved) {
                    CategoryPopupCard(
                        amount = amount,
                        payee = payee,
                        onCategoryChosen = { category ->
                            viewModel.save(amount, payee, category, timestamp)
                            chosenCategory = category
                            saved = true
                        },
                        onDismiss = { finish() }
                    )
                } else {
                    SavedConfirmationCard(
                        amount = amount,
                        category = chosenCategory!!,
                        onDone = { finish() }
                    )
                }
            }
        }
    }

    private fun showOverLockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(KeyguardManager::class.java)
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }
}
