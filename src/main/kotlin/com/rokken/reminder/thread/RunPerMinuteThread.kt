package com.rokken.reminder.thread

import java.time.LocalDateTime
import java.time.ZoneOffset

class RunPerMinuteThread(private val offset: String) : Thread() {
    override fun run() {
        while (true) {
            val localDateTime = LocalDateTime.now()
            val now = localDateTime.toInstant(ZoneOffset.of(offset)).epochSecond

            val nextTime = LocalDateTime.now()
                .withSecond(0)
                .plusMinutes(1)
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