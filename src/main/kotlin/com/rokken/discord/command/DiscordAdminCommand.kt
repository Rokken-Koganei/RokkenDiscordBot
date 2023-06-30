package com.rokken.discord.command

import com.rokken.discord.DiscordMain
import com.rokken.discord.DiscordMigration
import com.rokken.discord.role.RoleManager
import com.rokken.discord.message.FirstMessage
import com.rokken.discord.message.IntentionMessage
import com.rokken.discord.role.GradeRole
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
        const val SUBCOMMAND_MIGRATION = "migration"
        const val SUBCOMMAND_MIGRATION_DONE = "migdone"

        private val userOption = OptionData(OptionType.USER, "user", "ユーザー名").setRequired(true)

        val SUB_CMD_LIST = mutableListOf(
            SubcommandData(SUBCOMMAND_ADD, "幹部権限を付与する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_DEL, "幹部権限を剥奪する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_FIRST, "初期ロール付与用メッセージを送信する。")
                .addOptions(userOption),
            SubcommandData(SUBCOMMAND_HELP, "幹部コマンドのヘルプを表示する。"),
            SubcommandData(SUBCOMMAND_BROADCAST_HELP, "幹部コマンドのヘルプを全ての人が見えるように表示する。"),
            SubcommandData(SUBCOMMAND_MIGRATION, "年度が変わるときに必ず実行すること。ロール等の更新を行う。")
                .addOptions(OptionData(OptionType.STRING, "最終確認", "本当にいい？").setRequired(false)),
            SubcommandData(SUBCOMMAND_MIGRATION_DONE, "Migration 2 週間後に実行してください。")
                .addOptions(OptionData(OptionType.STRING, "最終確認", "本当にいい？").setRequired(false))
        )
    }

    private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (!CheckPermission.hasCommandPermission(event, RoleManager.ADMIN)) return

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
                val user = event.options[0].asUser
                FirstMessage().send(user)
                event.reply("<@${user.id}> に、初期設定文を送信しました。").setEphemeral(true).queue()
            }

            SUBCOMMAND_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(true).queue()

            SUBCOMMAND_BROADCAST_HELP -> event.replyEmbeds(helpEmbed()).setEphemeral(false).queue()

            SUBCOMMAND_MIGRATION -> {
                val gr = GradeRole()
                val options = event.options
                val msg = "/admin migration confirm で世代交代プログラムが実行されます。"

                when (options.size) {
                    0 -> {
                        val rtMsg = gr.migrate(false)
                        event.reply("「変更前の期 = <@&${rtMsg.first}>, 変更後の期 = ${rtMsg.second}」この値が正常であれば、\n$msg").setEphemeral(false).queue()
                    }
                    1 -> {
                        when (options[0].asString) {
                            "confirm" -> {
                                event.reply("実行します...").setEphemeral(false).queue()

                                gr.migrate(true)
                                DiscordMigration.run()
                                for (mem in DiscordMigration.getList()) {
                                    logger.info("Looking for $mem")
                                    DiscordMain.rokkenGuild.retrieveMemberById(mem).queue {
                                        logger.info("Sending message to ${it?.user?.id}")
                                        IntentionMessage().send(it.user)
                                    }
                                }
                            }
                            else -> event.reply(msg).setEphemeral(false).queue()
                        }
                    }
                    else -> event.reply("引数多すぎ問題発生中。\n $msg").setEphemeral(false).queue()
                }
            }

            SUBCOMMAND_MIGRATION_DONE -> {
                val msg = "/admin migdone confirm でサーバーキックを完了します。"
                val options = event.options
                var memsStr = ""

                for (memId in DiscordMigration.getList())
                    memsStr += "<@$memId>\n"

                when(options.size) {
                    0 -> event.reply("$memsStr これらの方が回答していません。実行する場合は、\n$msg").setEphemeral(false).queue()

                    1 -> {
                        when (options[0].asString) {
                            "confirm" -> {
                                event.reply("実行します...").setEphemeral(false).queue()
                                for (mem in DiscordMigration.getList()) {
                                    DiscordMain.rokkenGuild.retrieveMemberById(mem).queue {
                                        if (it.idLong == 442539918380498964) return@queue
                                        DiscordMain.rokkenGuild.kick(it, "2 週間以内に反応がなかったため kick しました。\n再び参加する場合は幹部に問い合わせましょう。").queue()
                                    }
                                }
                                DiscordMigration.deleteFile()
                                event.reply("実行が完了しました。").setEphemeral(false).queue()
                            }
                            else -> event.reply(msg).setEphemeral(false).queue()
                        }
                    }
                }
            }

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
        embed.addField("`migration`", "年度が替わった際にロールなどを代替わりさせる。", true)
        embed.addField("`migdone`", "migration 実行後に動いていないユーザーを一斉キックする。", true)

        return embed.build()
    }
}