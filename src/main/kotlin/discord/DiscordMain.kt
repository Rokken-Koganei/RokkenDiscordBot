package discord

import data.DataManager
import discord.command.DiscordAdminCommand
import discord.command.DiscordCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import javax.security.auth.login.LoginException
import kotlin.math.log

class DiscordMain {
    private val botToken = DataManager.getBotToken()
    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)
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

            jda.awaitReady()
            logger.info("DiscordBot is ready!")

            addCommand(jda.getGuildById(965233617800413265)!!)

        } catch (e: LoginException) {
            e.printStackTrace()
        }
    }

    private fun addCommand(guild: Guild) {
        logger.info("adding commands...")

        val adminCommand = Commands.slash(DiscordAdminCommand.COMMAND_NAME, "幹部用コマンド")
            .addSubcommands(DiscordAdminCommand.SUB_CMD_LIST)

        guild.updateCommands()
            .addCommands(adminCommand)
            .queue()

        logger.info("commands added!")
    }

}