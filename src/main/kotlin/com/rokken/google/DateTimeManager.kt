package com.rokken.google

import com.google.api.client.util.DateTime
import java.text.SimpleDateFormat
import java.util.Calendar

class DateTimeManager {
    companion object {
        /**
         * Converts a String to Google DateTime.
         *
         * @param year String representation of the year.
         * @param month String representation of the month.
         * @param day String representation of the day.
         * @param hour String representation of the hour.
         * @param minute String representation of the minute.
         *
         * @return Google DateTime object
         */
        fun create(year: String, month: String, day: String, hour: String, minute: String): DateTime {
            // yyyy-MM-ddThh:mm:00+09:00 (JST)
            return DateTime("$year-$month-${day}T$hour:$minute:00+09:00")
        }

        /**
         * Get the date n days from today and get the range of that date
         *
         * @param n n days later from today
         *
         * @return n days later from today DateTimeRange objects
         */
        fun getNDaysLaterFromTodayStartEnd(n: Int): Pair<DateTime, DateTime> {
            val start = getNDaysLaterFromToday(n)
            val end = getNDaysLaterFromToday(n, "23", "59")
            return Pair(start, end)
        }

        /**
         * Get the date n days from today.
         *
         * @param n n days later from today
         *
         * @return n days later from today DateTime object
         */
        private fun getNDaysLaterFromToday(n: Int, hour: String="00", minute: String="00"): DateTime {
            val now = Calendar.getInstance()
            now.add(Calendar.DATE, n)
            val year = SimpleDateFormat("yyyy").format(now.time)
            val month = SimpleDateFormat("MM").format(now.time)
            val day = SimpleDateFormat("dd").format(now.time)
            return create(year, month, day, hour, minute)
        }
    }
}