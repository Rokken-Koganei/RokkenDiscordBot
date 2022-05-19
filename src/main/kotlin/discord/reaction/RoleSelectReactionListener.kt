package discord.reaction

import discord.DiscordJoin
import discord.RoleManager
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class RoleSelectReactionListener: ListenerAdapter() {
    private var first = true

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return

        val guild = DiscordJoin.joinedGuild
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
        if (!first) return

        roleManager.deleteLatestMessage(event.channel)
        event.channel.sendMessage("**__答えてくださってありがとうございました！__**\n__必ずサーバー内の <#965608973527035994> を読んでください！__\nそれでは、楽しんでください！").queue {
            it.jda.removeEventListener(this)
        }

        first = false
    }
}