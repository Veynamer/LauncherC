package app.lws.launcherc

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.text.format.DateFormat
import android.widget.RemoteViews
import com.android.launcher3.R

class VerticalClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateVerticalClockWidget(context, appWidgetManager, appWidgetId)
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

    private fun updateVerticalClockWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.vertical_clock_widget)

        // Установка текущего времени
        val currentTime = DateFormat.format("HH:mm", System.currentTimeMillis()).toString()
        views.setTextViewText(R.id.time, currentTime)
        views.setTextColor(R.id.time, Color.parseColor("#FFFFFF")) // Белый цвет для времени

        // Установка текущей даты
        val currentDate = DateFormat.format("MM/dd EEE", System.currentTimeMillis()).toString()
        views.setTextViewText(R.id.date, currentDate)
        views.setTextColor(R.id.date, Color.parseColor("#B0BEC5")) // Светло-серый для даты

        // Установка PendingIntent для нажатия на время
        val alarmsIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val timePendingIntent = PendingIntent.getActivity(context, 0, alarmsIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.time, timePendingIntent)

        // Установка PendingIntent для нажатия на дату
        val calendarUri = CalendarContract.CONTENT_URI.buildUpon().appendPath("time").build()
        val calendarIntent = Intent(Intent.ACTION_VIEW).setData(calendarUri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        val datePendingIntent = PendingIntent.getActivity(context, 1, calendarIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        views.setOnClickPendingIntent(R.id.date, datePendingIntent)

        // Обновление виджета
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
