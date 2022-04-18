package discord.reaction

import discord.DiscordJoin
import discord.RoleManager
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ThirdReactionListener: ListenerAdapter() {
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return

        val guild = DiscordJoin.joinedGuild
        val roleManager = RoleManager()

        // 学年
        val vo = guild.getRoleById("965235976114942034")
        val gt = guild.getRoleById("965235822800568320")
        val dr = guild.getRoleById("965235713777995856")
        val ba = guild.getRoleById("965235743125536789")
        val key = guild.getRoleById("965235681767096320")

        val message = event.channel.retrieveMessageById(event.messageId).complete()

        var selected = false

        when (event.reactionEmote.name) {
            "🎤" -> {
                roleManager.addRole(guild, user!!.id, vo!!)
                roleManager.deleteMessage(message)
                selected = true
            }
            "🎸" -> {
                roleManager.addRole(guild, user!!.id, gt!!)
                roleManager.deleteMessage(message)
                selected = true
            }
            "🥁" -> {
                roleManager.addRole(guild, user!!.id, dr!!)
                roleManager.deleteMessage(message)
                selected = true
            }
            "🪕" -> {
                roleManager.addRole(guild, user!!.id, ba!!)
                roleManager.deleteMessage(message)
                selected = true
            }
            "🎹" -> {
                roleManager.addRole(guild, user!!.id, key!!)
                roleManager.deleteMessage(message)
                selected = true
            }
        }

        if (selected) {
            event.channel.sendMessage("答えてくださってありがとうございました！\n楽しんでください！").queue()
        }
    }
}