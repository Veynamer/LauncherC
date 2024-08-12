package app.lws.launcherc

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.text.format.DateFormat
import com.android.launcher3.R

class VerticalClockWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val provider = ComponentName(context, javaClass)
        val widgetIds = appWidgetManager.getAppWidgetIds(provider)

        when (intent.action) {
            Intent.ACTION_LOCALE_CHANGED -> onUpdate(context, appWidgetManager, widgetIds)
        }
    }
}

private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.vertical_clock_widget)

    // Установка текущего времени
    val currentTime = DateFormat.format("HH:mm", System.currentTimeMillis()).toString()
    views.setTextViewText(R.id.time, currentTime)

    // Установка текущей даты
    val currentDate = DateFormat.format("MM/dd EEE", System.currentTimeMillis()).toString()
    views.setTextViewText(R.id.date, currentDate)

    // Установка PendingIntent для нажатия на время
    val timeIntent = Intent(context, YourTargetActivity::class.java) // Замените на вашу активность
    val timePendingIntent = PendingIntent.getActivity(context, 0, timeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.time, timePendingIntent)

    // Установка PendingIntent для нажатия на дату
    val dateIntent = Intent(context, YourTargetActivity::class.java) // Замените на вашу активность
    val datePendingIntent = PendingIntent.getActivity(context, 1, dateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.date, datePendingIntent)

    // Обновление виджета
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
