package com.rokken.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.appengine.AppEngineCredentials
import com.google.auth.http.HttpCredentialsAdapter
import com.rokken.Main
import java.io.FileInputStream
import java.util.*

class Connector {
    companion object {
        fun create(filePath: String): Calendar {
            val transport = GoogleNetHttpTransport.newTrustedTransport()
            val credentials = AppEngineCredentials.fromStream(FileInputStream(filePath))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR_EVENTS))

            return Calendar.Builder(
                transport,
                JacksonFactory.getDefaultInstance(),
                HttpCredentialsAdapter(credentials)
            )
                .setApplicationName(Main.APP_NAME)
                .build()
        }
    }
}