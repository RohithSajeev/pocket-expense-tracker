package com.rohith.pocket.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.rohith.pocket.MainActivity
import com.rohith.pocket.ui.screens.CategoryPopupActivity
import java.util.regex.Pattern

/**
 * Listens for system notifications and reacts specifically to Google Pay's
 * "payment sent" notifications. When one is spotted, it parses the amount and
 * payee, then fires a full-screen intent that opens CategoryPopupActivity —
 * this is what actually shows Pocket's "Choose a category" card on screen,
 * without needing the SYSTEM_ALERT_WINDOW overlay permission.
 */
class PaymentNotificationListener : NotificationListenerService() {

    companion object {
        private const val GPAY_PACKAGE = "com.google.android.apps.nbu.paisa.user"
        private const val CHANNEL_ID = "pocket_capture_channel"

        // Matches things like "You paid ₹450 to Zee Cafe" / "₹450 sent to Ramesh"
        private val AMOUNT_PATTERN: Pattern =
            Pattern.compile("₹\\s?([0-9]+(?:,[0-9]{2,3})*(?:\\.[0-9]{1,2})?)")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != GPAY_PACKAGE) return

        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()
        val fullText = "$title $text"

        // Only react to outgoing payments — skip "received" / promo notifications.
        val isPaymentSent = fullText.contains("paid", ignoreCase = true) ||
            fullText.contains("sent", ignoreCase = true)
        if (!isPaymentSent) return

        val amount = extractAmount(fullText) ?: return
        val payee = extractPayee(fullText)

        launchCategoryPopup(amount, payee)
    }

    private fun extractAmount(text: String): Double? {
        val matcher = AMOUNT_PATTERN.matcher(text)
        if (!matcher.find()) return null
        val raw = matcher.group(1)?.replace(",", "") ?: return null
        return raw.toDoubleOrNull()
    }

    private fun extractPayee(text: String): String {
        // "You paid ₹450 to Zee Cafe" -> "Zee Cafe"
        val toIndex = text.lastIndexOf(" to ")
        return if (toIndex != -1) text.substring(toIndex + 4).trim() else "Unknown"
    }

    private fun launchCategoryPopup(amount: Double, payee: String) {
        ensureChannel()

        val popupIntent = Intent(this, CategoryPopupActivity::class.java).apply {
            putExtra(CategoryPopupActivity.EXTRA_AMOUNT, amount)
            putExtra(CategoryPopupActivity.EXTRA_PAYEE, payee)
            putExtra(CategoryPopupActivity.EXTRA_TIMESTAMP, System.currentTimeMillis())
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NO_HISTORY
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            popupIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // swap for the Pocket app icon
            .setContentTitle("Categorize this expense")
            .setContentText("₹${amount.toInt()} to $payee")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL) // treated as urgent, like an incoming call
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .build()

        getSystemService(NotificationManager::class.java).notify(amount.hashCode(), notification)
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expense capture",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows the category popup right after a payment"
            }
            manager.createNotificationChannel(channel)
        }
    }
}
