package com.rokken.reminder

import com.rokken.google.calendar.GetEvent
import com.rokken.reminder.thread.RunPerDayThread
import com.rokken.reminder.thread.RunPerMinuteThread

class ReminderMain {
    private val offset = "+9"

    private val perDayRunnable = Runnable {
//        GetEvent.getNDaysLaterEvent()
    }

    fun start() {
        val runPerMinuteThread = RunPerMinuteThread(offset)
        val runPerDayThread = RunPerDayThread(offset)

        runPerMinuteThread.start()
        runPerDayThread.start()
    }
}