package app.lws.launcherc

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.widget.RemoteViews
import com.android.launcher3.R

class BatteryWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val batteryLevel = getBatteryLevel(context)

        val views = RemoteViews(context.packageName, R.layout.battery_widget_layout)
        views.setTextViewText(R.id.battery_percentage, "%.0f%%".format(batteryLevel))

        val batteryDrawableId = when {
            batteryLevel >= 100 -> R.drawable.battery_full
            batteryLevel >= 90 -> R.drawable.battery_90
            batteryLevel >= 80 -> R.drawable.battery_80
            batteryLevel >= 70 -> R.drawable.battery_70
            batteryLevel >= 60 -> R.drawable.battery_60
            batteryLevel >= 50 -> R.drawable.battery_50
            batteryLevel >= 40 -> R.drawable.battery_40
            batteryLevel >= 30 -> R.drawable.battery_30
            batteryLevel >= 20 -> R.drawable.battery_20
            batteryLevel >= 10 -> R.drawable.battery_10
            else -> R.drawable.battery_empty
        }
        views.setImageViewResource(R.id.battery_icon, batteryDrawableId)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getBatteryLevel(context: Context): Float {
        return if (context.checkSelfPermission(Manifest.permission.BATTERY_STATS) == PackageManager.PERMISSION_GRANTED) {
            val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            level * 100 / scale.toFloat()
        } else {
            -1f
        }
    }
}