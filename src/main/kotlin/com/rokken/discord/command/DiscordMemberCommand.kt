package com.rokken.discord.command

import com.rokken.discord.RoleManager
import com.rokken.discord.message.RoleMessage
import com.rokken.discord.reaction.RoleSelectReactionListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import java.awt.Color

class DiscordMemberCommand: ListenerAdapter() {
    companion object {
        const val COMMAND_NAME = "member"

        const val SUBCOMMAND_RESET = "reset"
        const val SUBCOMMAND_HELP = "help"

        val SUB_CMD_LIST = mutableListOf(
            SubcommandData(SUBCOMMAND_RESET, "楽器ロールをリセットして再選択する。"),
            SubcommandData(SUBCOMMAND_HELP, "部員コマンドのヘルプを表示する。")
        )
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (!CheckPermission.hasCommandPermission(event, RoleManager.MEMBER)) return

        val guild = event.guild!!

        // member command のみを受け付ける
        if (event.name != COMMAND_NAME) return

        if (event.subcommandName.isNullOrBlank()) {
            event.reply("**__オプションを指定してください。__**\n/admin help でヘルプを表示します。").setEphemeral(true).queue()
            return
        }

        when (event.subcommandName) {
            SUBCOMMAND_RESET -> {
                val member = event.member!!
                RoleManager().delInstRole(guild, member)
                RoleMessage().send(member.user)
                RoleSelectReactionListener.isReset = true
                RoleSelectReactionListener.queue.add(member.user)
                event.reply("<@${member.id}> の DM へロール選択テキストを送信しました。").setEphemeral(true).queue()
            }

            SUBCOMMAND_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(true).queue()

            else -> event.reply("不明なコマンドです。\n/$COMMAND_NAME $SUBCOMMAND_HELP でヘルプを表示します。").setEphemeral(true).queue()
        }

        logger.info("${event.user.name} issued ${event.commandString}")
    }

    private fun helpEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("`/$COMMAND_NAME` コマンドの使い方")
        embed.setDescription("`/$COMMAND_NAME <option>`\n\n**__このコマンドは部員しか使用できません。__**\n<option> には以下のいずれかを入れてください。")

        embed.addField("`$SUBCOMMAND_RESET`", "楽器ロールをリセットして再選択する。", true)
        embed.addField("`help`", "コマンドヘルプを表示します。", true)

        return embed.build()
    }
}