package com.rokken.google.calendar

import com.rokken.Main
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime

class SetEvent {
    companion object {
        fun createEvent(title: String, description: String="", start: DateTime, end: DateTime): Event {
            val startTime = EventDateTime().setDateTime(start)
            val endTime = EventDateTime().setDateTime(end)

            return Event()
                .setSummary(title)
                .setDescription(description)
                .setStart(startTime)
                .setEnd(endTime)
        }

        fun registerEvent(event: Event) {
            Main.service.events().insert(Main.calendarAddress, event).execute()
        }
    }
}