package discord.reaction

import discord.DiscordMain
import discord.RoleManager
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class RoleSelectReactionListener: ListenerAdapter() {
    companion object {
        val queue = ArrayList<User>()

        var isReset = false
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return
        // キューの中にそのユーザーがいるかどうか
        if (!queue.contains(user)) return

        val guild = DiscordMain.rokkenGuild
        val roleManager = RoleManager()

        // 学年
        val vo = guild.getRoleById(RoleManager.VOCAL)
        val gt = guild.getRoleById(RoleManager.GUITAR)
        val dr = guild.getRoleById(RoleManager.DRUM)
        val ba = guild.getRoleById(RoleManager.BASE)
        val key = guild.getRoleById(RoleManager.KEY)

        when (event.reactionEmote.name) {
            "🎤" -> {
                roleManager.addRole(guild, user!!.id, vo!!)
            }
            "🎸" -> {
                roleManager.addRole(guild, user!!.id, gt!!)
            }
            "🥁" -> {
                roleManager.addRole(guild, user!!.id, dr!!)
            }
            "🪕" -> {
                roleManager.addRole(guild, user!!.id, ba!!)
            }
            "🎹" -> {
                roleManager.addRole(guild, user!!.id, key!!)
            }
            "❌" -> {
                selected(roleManager, event)
            }
        }
    }

    private fun selected(roleManager: RoleManager, event: MessageReactionAddEvent) {
        roleManager.deleteLatestMessage(event.channel)

        var sendText = "**__答えてくださってありがとうございました！__**\n__必ずサーバー内の <#965608973527035994> を読んでください！__\nそれでは、楽しんでください！"
        if (isReset) {
            sendText = "楽器ロールの再選択が完了しました！"
        }

        event.channel.sendMessage(sendText).queue()

        isReset = false
        val user = event.user!!
        queue.remove(user)
    }
}