package com.rokken

import com.google.api.services.calendar.Calendar
import com.rokken.data.ResourceExporter
import com.rokken.data.YamlLoader
import com.rokken.discord.DiscordMain
import com.rokken.google.Connector
import com.rokken.google.DateTimeManager
import com.rokken.google.calendar.GetEvent
import com.rokken.google.calendar.SetEvent
import com.rokken.reminder.ReminderMain
import java.io.File

class Main {
    companion object {
        const val APP_NAME = "Rokken Discord Bot"
        const val calendarAddress = ""

        val DIR_PATH = System.getProperty("user.dir") + File.separator + "RokkenDiscordBot"
        lateinit var service: Calendar
    }

    // throwsでやると、jarにした時見つからなくてエラーはくっぽい？
    fun start() {
        ResourceExporter.export()
        YamlLoader.load(DIR_PATH + File.separator + "config.yml")
        val discordMain = DiscordMain()
        // val reminderMain = ReminderMain()

        discordMain.start()
//        reminderMain.start()
    }

    private val keyFilePath = "tutorial/key.json"

    fun start2() {
        service = Connector.create(keyFilePath)

        val startDateTime = DateTimeManager.create("2022", "05", "26", "09", "00")
        val endDateTime = DateTimeManager.create("2022", "05", "26", "10", "00")

        val summary = "Tutorial"
        val description = "This is a tutorial event"

        val event = SetEvent.createEvent(summary, description, startDateTime, endDateTime)
        SetEvent.registerEvent(event)

        val events = GetEvent.getTodayEvent()

        for (str in events.items) {
            println("${str.summary}, ${str.description}, ${str.start.dateTime}, ${str.end.dateTime}, ${str.htmlLink}")
        }
        println(events)
    }
}

fun main(){
    Main().start()
}