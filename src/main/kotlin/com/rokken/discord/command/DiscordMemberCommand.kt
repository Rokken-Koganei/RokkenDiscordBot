package com.rokken.discord.command

import com.rokken.discord.role.RoleManager
import com.rokken.discord.message.RoleMessage
import com.rokken.discord.reaction.RoleSelectReactionListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import java.awt.Color

class DiscordMemberCommand: ListenerAdapter() {
    companion object {
        const val QUESTION_CHANNEL = 983646310693498890
        const val MINECRAFT_ROLE = 1125053904044965978
        const val COMMAND_NAME = "member"

        const val SUBCOMMAND_RESET = "reset"
        const val SUBCOMMAND_QUESTION = "question"
        const val SUBCOMMAND_HELP = "help"
        const val SUBCOMMAND_MINECRAFT = "minecraft"

        private val questionOption = OptionData(OptionType.STRING, "question", "質問内容")
        private val minecraftOption = OptionData(OptionType.STRING, "join-exit", "参加or退会").setRequired(true)

        val SUB_CMD_LIST = mutableListOf(
            SubcommandData(SUBCOMMAND_RESET, "楽器ロールをリセットして再選択する。"),
            SubcommandData(SUBCOMMAND_QUESTION, "質問箱に匿名で質問を投げれます。")
                .addOptions(questionOption),
            SubcommandData(SUBCOMMAND_HELP, "部員コマンドのヘルプを表示する。"),
            SubcommandData(SUBCOMMAND_MINECRAFT, "マイクラサーバーに参加または退会する。")
                .addOptions(minecraftOption)
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

            SUBCOMMAND_QUESTION -> {
                val message = event.options[0].asString
                val channel = guild.getTextChannelById(QUESTION_CHANNEL)!!
                channel.sendMessage(message).queue()
                event.reply("質問箱に質問を投げました。").setEphemeral(true).queue()
            }

            SUBCOMMAND_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(true).queue()

            SUBCOMMAND_MINECRAFT -> {
                val option = event.options[0].asString
                val member = event.member!!
                val role = guild.getRoleById(MINECRAFT_ROLE)!!

                when (option) {
                    "join" -> {
                        RoleManager().addRole(guild, member, role)
                        event.reply("マイクラロールを付与しました。").setEphemeral(true).queue()
                    }

                    "exit" -> {
                        RoleManager().delRole(guild, member, role)
                        event.reply("マイクラロールを剥奪しました。").setEphemeral(true).queue()
                    }

                    else -> event.reply("不明なオプションです。join または exit を入力してください。").setEphemeral(true).queue()
                }
            }

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
        embed.addField("`$SUBCOMMAND_QUESTION`", "質問箱に匿名で質問を投げれます。", true)
        embed.addField("`$SUBCOMMAND_MINECRAFT`", "マイクラサーバーに参加または退会する。", true)

        return embed.build()
    }
}