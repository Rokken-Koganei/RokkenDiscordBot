package discord

import data.DataManager
import discord.command.DiscordAdminCommand
import discord.command.DiscordCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import javax.security.auth.login.LoginException

class DiscordMain {
    private val botToken = DataManager.getBotToken()
    private lateinit var jda: JDA

    fun start() {
        try {
            //Login
            jda = JDABuilder
                .createLight(
                    botToken,
                    GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES
                )
                .addEventListeners(
                    DiscordCommand(),
                    DiscordAdminCommand(),
                    DiscordJoin()
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.playing("作業"))
                .build()
        } catch (e: LoginException) {
            e.printStackTrace()
        }
    }
}