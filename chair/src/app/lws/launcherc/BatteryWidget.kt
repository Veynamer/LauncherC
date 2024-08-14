package app.lws.launcherc

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
        val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = level * 100 / scale.toFloat()

        val views = RemoteViews(context.packageName, R.layout.battery_widget_layout)
        views.setTextViewText(R.id.battery_percentage, "%.0f%%".format(batteryPct))
        
        val batteryDrawableId = when {
            batteryPct >= 100 -> R.drawable.battery_full
            batteryPct >= 90 -> R.drawable.battery_90
            batteryPct >= 80 -> R.drawable.battery_80
            batteryPct >= 70 -> R.drawable.battery_70
            batteryPct >= 60 -> R.drawable.battery_60
            batteryPct >= 50 -> R.drawable.battery_50
            batteryPct >= 40 -> R.drawable.battery_40
            batteryPct >= 30 -> R.drawable.battery_30
            batteryPct >= 20 -> R.drawable.battery_20
            batteryPct >= 10 -> R.drawable.battery_10
            else -> R.drawable.battery_empty
        }
        views.setImageViewResource(R.id.battery_icon, batteryDrawableId)
        
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}