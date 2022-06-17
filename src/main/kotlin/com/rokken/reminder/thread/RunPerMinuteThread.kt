package com.rokken.reminder.thread

import java.time.LocalDateTime
import java.time.ZoneOffset

class RunPerMinuteThread(private val runnable: Runnable) : Thread() {
    override fun run() {
        while (true) {
            val localDateTime = LocalDateTime.now()
            val now = localDateTime.toInstant(ZoneOffset.of("Asia/Tokyo")).epochSecond
            localDateTime.withSecond(0)
            localDateTime.plusMinutes(1)
            val minuteLater = localDateTime.toInstant(ZoneOffset.of("Asia/Tokyo")).epochSecond

            val sleepTime = minuteLater - now

            try {
                sleep(sleepTime)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            runnable.run()
        }
    }
}