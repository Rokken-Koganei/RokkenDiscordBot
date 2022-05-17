package discord.command

import discord.DiscordJoin
import discord.RoleManager
import discord.message.FirstMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import java.awt.Color

class DiscordAdminCommand: ListenerAdapter() {
    companion object {
        const val COMMAND_NAME = "admin"

        const val SUBCOMMAND_ADD = "add"
        const val SUBCOMMAND_DEL = "del"
        const val SUBCOMMAND_FIRST = "first"
        const val SUBCOMMAND_HELP = "help"
        const val SUBCOMMAND_BROADCAST_HELP = "bhelp"

        private val userOption = OptionData(OptionType.USER, "user", "ユーザー名").setRequired(true)

        val SUB_CMD_LIST = mutableListOf(
            SubcommandData(SUBCOMMAND_ADD, "幹部権限を付与する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_DEL, "幹部権限を剥奪する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_FIRST, "初期ロール付与用メッセージを送信する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_HELP, "幹部コマンドのヘルプを表示する。"),
            SubcommandData(SUBCOMMAND_BROADCAST_HELP, "幹部コマンドのヘルプを全ての人が見えるように表示する。")
        )
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (CheckPermission.hasCommandPermission(event, RoleManager.ADMIN)) return

        val guild = event.guild!!
        val admin = guild.getRoleById(RoleManager.ADMIN)!!

        // admin command のみを受け付ける
        if (event.name != COMMAND_NAME) return

        if (event.subcommandName.isNullOrBlank()) {
            event.reply("**__オプションを指定してください。__**\n/admin help でヘルプを表示します。").setEphemeral(true).queue()
            return
        }

        when (event.subcommandName) {
            SUBCOMMAND_ADD -> {
                val member = event.options[0].asMember!!
                RoleManager().addRole(guild, member, admin)
                event.reply("<@${member.id}> を幹部にしました。").queue()
            }

            SUBCOMMAND_DEL -> {
                val member = event.options[0].asMember!!
                RoleManager().delRole(guild, member, admin)
                event.reply("<@${member.id}> を幹部から除外しました。").queue()
            }

            SUBCOMMAND_FIRST -> {
                DiscordJoin.joinedGuild = guild

                val user = event.options[0].asUser
                FirstMessage().firstMessage(user)
                event.reply("<@${user.id}> に、初期設定文を送信しました。").setEphemeral(true).queue()
            }

            SUBCOMMAND_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(true).queue()

            SUBCOMMAND_BROADCAST_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(false).queue()

            else -> event.reply("不明なコマンドです。\n/admin help でヘルプを表示します。").setEphemeral(true).queue()
        }

        logger.info("${event.user.name} issued ${event.commandString}")
    }

    private fun helpEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("`/admin` コマンドの使い方")
        embed.setDescription("`/admin <option>`\n\n**__このコマンドは幹部しか使用できません。__**\n<option> には以下のいずれかを入れてください。\n<userId> には <@人の名前> を入れてください。")

        embed.addField("`add <userId>`", "幹部権限を指定ユーザーに付与します。", true)
        embed.addField("`del <userId>`", "幹部権限を指定ユーザーから剥奪します。", true)
        embed.addField("`first <userId>`", "指定ユーザーに初期設定文を DM に送りつけます。", true)
        embed.addField("`help`", "コマンドヘルプを表示します。", true)

        return embed.build()
    }
}