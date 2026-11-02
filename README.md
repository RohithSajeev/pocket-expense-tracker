# Pocket

A personal expense tracker that listens for Google Pay's payment
notifications and asks you to categorize each spend the instant it happens.

## How to open it
1. Unzip this folder anywhere on your computer.
2. Open Android Studio → **Open** → select the `PocketApp` folder.
3. Let Gradle sync (it will download the dependencies listed in
   `app/build.gradle.kts`).
4. Run on a real device or an emulator with Google Play services + a Gmail
   account signed in to Google Pay, minSdk 26 (Android 8) or above.

## First-run setup on the device
1. Install GPay and make at least one payment so a notification exists to test with.
2. Open **Pocket → Settings → Notification Access** — this deep-links to
   Android's system settings. Turn the toggle on for Pocket.
3. Send a payment from GPay. Pocket's `PaymentNotificationListener` will catch
   it and pop the category card on screen automatically.

## What's fully wired up
- Room database (`Transaction`, `TransactionDao`, `AppDatabase`)
- `NotificationListenerService` that detects GPay's "paid"/"sent" notifications
  and parses the amount + payee with a regex
- The popup flow: full-screen intent → `CategoryPopupActivity` →
  category card → saved-confirmation card, all in Compose
- Home dashboard, Transaction History (with category filters), Insights
  (donut chart + top category), Settings (with a live notification-access
  status check)
- A single Material 3 theme matching the reference design (green accent,
  white cards, colored category chips)

## What's intentionally left as a stub — build these next
- **GPay's exact notification wording** varies by device/App version. Log
  `sbn.notification.extras` for a real GPay notification (Logcat) and adjust
  the regex/keywords in `PaymentNotificationListener` if it doesn't match.
- **Monthly bar chart** on `MonthlyScreen` currently draws placeholder bars —
  wire it to a real "totals per month, last 6 months" Room query.
- **App icon** — currently uses a system drawable as a placeholder in the
  notification; swap in a real launcher icon before you're happy with it.
- Editing/deleting a saved transaction isn't built yet (only insert + read).
