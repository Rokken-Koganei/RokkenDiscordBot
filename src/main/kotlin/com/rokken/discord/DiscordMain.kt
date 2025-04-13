package com.rokken.discord

import com.rokken.data.DataManager
import com.rokken.discord.command.DiscordAdminCommand
import com.rokken.discord.command.DiscordCommand
import com.rokken.discord.command.DiscordMemberCommand
import com.rokken.discord.listener.JoinMessageListener
import com.rokken.discord.reaction.GradeSelectReactionListener
import com.rokken.discord.reaction.IntentionReactionListener
import com.rokken.discord.reaction.MemberSelectReactionListener
import com.rokken.discord.reaction.RoleSelectReactionListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import javax.security.auth.login.LoginException

class DiscordMain {
    companion object {
        lateinit var rokkenGuild: Guild
    }
    private val serverId = DataManager.getServerId()
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
//                    DiscordCommand(),
                    DiscordAdminCommand(),
                    DiscordMemberCommand(),
//                    DiscordJoin(),
//                    MemberSelectReactionListener(),
//                    GradeSelectReactionListener(),
//                    RoleSelectReactionListener(),
                    JoinMessageListener(),
                    IntentionReactionListener()
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.competing("ロッ研"))
                .setStatus(OnlineStatus.ONLINE)
                .build()

            jda.awaitReady()

            rokkenGuild = jda.getGuildById(serverId)!!
            addCommand(rokkenGuild)

            DiscordMigration.loadFile()

            logger.info("DiscordBot is ready!")
        } catch (e: LoginException) {
            e.printStackTrace()
        }
    }

    private fun addCommand(guild: Guild) {
        logger.info("adding commands...")

        val adminCommand = Commands.slash(DiscordAdminCommand.COMMAND_NAME, "幹部用コマンド")
            .addSubcommands(DiscordAdminCommand.SUB_CMD_LIST)

        val memberCommand = Commands.slash(DiscordMemberCommand.COMMAND_NAME, "部員用コマンド")
            .addSubcommands(DiscordMemberCommand.SUB_CMD_LIST)

        guild.updateCommands()
            .addCommands(
                adminCommand,
                memberCommand
            )
            .queue()

        logger.info("commands added!")
    }

}