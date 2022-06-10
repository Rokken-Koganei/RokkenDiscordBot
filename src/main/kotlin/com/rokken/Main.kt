package com.rokken

import com.rokken.data.ResourceExporter
import com.rokken.data.YamlLoader
import com.rokken.discord.DiscordMain
import java.io.File

class Main {
    companion object {
        val DIR_PATH = System.getProperty("user.dir") + File.separator + "RokkenDiscordWelcomeBot"
    }

    // throwsでやると、jarにした時見つからなくてエラーはくっぽい？
    fun start() {
        ResourceExporter.export()
        YamlLoader.load(DIR_PATH + File.separator + "config.yml")
        val discordMain = DiscordMain()

        discordMain.start()
    }
}

fun main(){
    Main().start()
}