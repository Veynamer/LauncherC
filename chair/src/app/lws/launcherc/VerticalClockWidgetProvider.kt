package app.lws.launcherc

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import android.text.format.DateFormat
import com.android.launcher3.R

class VerticalClockWidgetProvider : AppWidgetProvider() {
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

    val currentTime = DateFormat.format("HH:mm", System.currentTimeMillis()).toString()
    views.setTextViewText(R.id.time, currentTime)
    views.setTextColor(R.id.time, Color.parseColor("#FFFFFF")) // Белый цвет для времени

    val currentDate = DateFormat.format("MM/dd EEE", System.currentTimeMillis()).toString()
    views.setTextViewText(R.id.date, currentDate)
    views.setTextColor(R.id.date, Color.parseColor("#B0BEC5")) // Светло-серый для даты

    val timeIntent = Intent(context, MainActivity::class.java) // Замените на вашу активность
    val timePendingIntent = PendingIntent.getActivity(context, 0, timeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.time, timePendingIntent)

    val dateIntent = Intent(context, MainActivity::class.java) // Замените на вашу активность
    val datePendingIntent = PendingIntent.getActivity(context, 1, dateIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.date, datePendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}
