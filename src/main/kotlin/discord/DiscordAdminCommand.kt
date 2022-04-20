package discord

import discord.message.FirstMessage
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class DiscordAdminCommand: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild) return

        val guild = event.guild
        val admin = guild.getRoleById(RoleManager.ADMIN)!!

        // ボットからのメッセージは見ない
        if (event.author.isBot) return

        // ロールが幹部でなければ無視
        if (!event.member!!.roles.contains(admin)) return

        val roleManager = RoleManager()
        val msg = event.message.contentRaw.split(" ")
        val command = "/admin"

        if (msg[0] != command) return
        if (msg.size < 2) {
            event.channel.sendMessage("コマンドが正しくありません。\ntype: /admin help").queue()
            return
        }

        when (msg[1]) {
            "add" -> { // /admin add の時
                if (msg.size < 3) {
                    event.channel.sendMessage("コマンドが正しくありません。\ntype: /admin help").queue()
                    return
                }
                val userId = msg[2].substring(2, msg[2].length - 1)
                event.guild.loadMembers().onSuccess { members ->
                    val member = members.find { it.user.id == userId }!!
                    if (msg.size == 4 && msg[3] == "confirm") {
                        roleManager.addRole(guild, member, admin)
                        event.channel.sendMessage("<@${member.id}> を幹部にしました。").queue()
                    } else {
                        event.channel.sendMessage("<@${member.id}> を幹部にしますか？\n${command} の最後に confirm を追記してください。").queue()
                    }
                }
            }

            "del" -> { // /admin add の時
                if (msg.size < 3) {
                    event.channel.sendMessage("コマンドが正しくありません。\ntype: /admin help").queue()
                    return
                }
                val userId = msg[2].substring(2, msg[2].length - 1)
                event.guild.loadMembers().onSuccess { members ->
                    val member = members.find { it.user.id == userId }!!
                    roleManager.delRole(guild, member, admin)
                    event.channel.sendMessage("<@${member.id}> を幹部から除外しました。").queue()
                }
            }

            "first" -> {
                if (msg.size < 3) {
                    event.channel.sendMessage("コマンドが正しくありません。\ntype: /admin help").queue()
                    return
                }
                DiscordJoin.joinedGuild = guild
                val userId = msg[2].substring(2, msg[2].length - 1)
                event.guild.loadMembers().onSuccess { members ->
                    val member = members.find { it.user.id == userId }!!
                    FirstMessage().firstMessage(member.user)
                }
            }

            "help" -> {
                event.channel.sendMessageEmbeds(helpEmbed()).complete()
            }
        }
    }

    private fun helpEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("`/admin` コマンドの使い方")
        embed.setDescription("`/admin <option>`\n\n**__このコマンドは幹部しか使用できません。__**\n<option> には以下のいずれかを入れてください。\n<userId> には <@人の名前> を入れてください。")

        embed.addField("`add <userId> confirm`", "幹部権限を指定ユーザーに付与します。", true)
        embed.addField("`del <userId>`", "幹部権限を指定ユーザーから剥奪します。", true)
        embed.addField("`first <userId>`", "指定ユーザーに初期設定文を DM に送りつけます。", true)
        embed.addField("`help`", "コマンドヘルプを表示します。", true)

        return embed.build()
    }
}