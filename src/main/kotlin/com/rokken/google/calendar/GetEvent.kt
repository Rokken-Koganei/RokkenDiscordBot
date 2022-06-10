package com.rokken.google.calendar

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Events
import com.rokken.Main
import com.rokken.google.DateTimeManager

class GetEvent {
    companion object {
        /**
         * Get the list of events from start time to end time.
         *
         * @param start The start date.
         * @param end The end date.
         *
         * @return The list of events.
         */
        fun getDuringEvent(start: DateTime, end: DateTime): Events {
            return Main.service.events().list(Main.calendarAddress)
                .setTimeMin(start)
                .setTimeMax(end)
                .setSingleEvents(true)
                .execute()
        }

        /**
         * Get the list of today events.
         *
         * @return The list of events.
         */
        fun getTodayEvent(): Events {
            val time = DateTimeManager.getNDaysLaterFromTodayStartEnd(0)
            return getDuringEvent(time.first, time.second)
        }

        /**
         * Get the list of n days later events.
         *
         * @param n The number of days later.
         *
         * @return The list of events.
         */
        fun getNDaysLaterEvent(n: Int): Events {
            val time = DateTimeManager.getNDaysLaterFromTodayStartEnd(n)
            return getDuringEvent(time.first, time.second)
        }
    }
}