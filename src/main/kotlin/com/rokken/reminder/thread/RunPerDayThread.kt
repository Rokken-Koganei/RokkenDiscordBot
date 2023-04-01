package com.rokken.reminder.thread

import java.time.LocalDateTime
import java.time.ZoneOffset

class RunPerDayThread(private val offset: String) : Thread() {
    override fun run() {
        while (true) {
            val localDateTime = LocalDateTime.now()
            val now = localDateTime.toInstant(ZoneOffset.of(offset)).epochSecond

            val nextTime = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusDays(1)
                .toInstant(ZoneOffset.of(offset)).epochSecond

            val sleepTime = nextTime - now

            try {
                sleep(sleepTime * 1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}