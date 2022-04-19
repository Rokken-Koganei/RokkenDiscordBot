package discord

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordAdminCommand: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.isFromGuild) return

        val guild = event.guild
        val admin = guild.getRoleById(RoleManager.ADMIN)!!

        // ボットからのメッセージは見ない
        if (event.author.isBot) return

        val member = event.member

        // ロールが幹部でなければ無視
        if (!member!!.roles.contains(admin)) return

        val roleManager = RoleManager()
        val msg = event.message.contentRaw.split(" ")
        val command = "/admin"

        if (msg[0] != command) return
        if (msg.size < 3) {
            event.channel.sendMessage("コマンドが正しくありません。\ntype: /admin help").queue()
            return
        }

        when (msg[1]) {
            "add" -> { // !admin add の時
                val userId = msg[2].substring(2, msg[2].length - 1)
                event.guild.loadMembers().onSuccess { members ->
                    val user = members.find { it.user.id == userId }!!
                    if (msg.size == 4 && msg[3] == "confirm") {
                        roleManager.addRole(guild, user, admin)
                        event.channel.sendMessage("<@${user.id}> を幹部にしました。").queue()
                    } else {
                        event.channel.sendMessage("<@${user.id}> を幹部にしますか？\n${command} の最後に confirm を追記してください。").queue()
                    }
                }
            }

            "del" -> { // !admin add の時
                val userId = msg[2].substring(2, msg[2].length - 1)
                event.guild.loadMembers().onSuccess { members ->
                    val user = members.find { it.user.id == userId }!!
                    roleManager.delRole(guild, user, admin)
                    event.channel.sendMessage("<@${user.id}> を幹部から除外しました。").queue()
                }
            }
        }
    }
}