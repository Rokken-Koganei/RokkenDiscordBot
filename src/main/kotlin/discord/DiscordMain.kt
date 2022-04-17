package discord

import data.DataManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import javax.security.auth.login.LoginException

class DiscordMain {
    private val botToken = DataManager.getBotToken()
    private lateinit var jda: JDA

    fun start() {
        try {
            //Login
            jda = JDABuilder.createLight(botToken, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(DiscordCommand())
                .setActivity(Activity.playing("作業"))
                .build()
        } catch (e: LoginException) {
            e.printStackTrace()
        }
    }

    fun notify(message: String) {

    }
}