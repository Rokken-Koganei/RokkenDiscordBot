package discord.reaction

import discord.DiscordMain
import discord.RoleManager
import discord.message.RoleMessage
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GradeSelectReactionListener: ListenerAdapter() {
    private var first = true

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return

        val guild = DiscordMain.rokkenGuild
        val roleManager = RoleManager()

        // 学年
        val grade1 = guild.getRoleById(RoleManager.GRADE_1)
        val grade2 = guild.getRoleById(RoleManager.GRADE_2)
        val grade3 = guild.getRoleById(RoleManager.GRADE_3)
        val grade4 = guild.getRoleById(RoleManager.GRADE_4)

        when (event.reactionEmote.name) {
            "1️⃣" -> {
                roleManager.addRole(guild, user!!.id, grade1!!)
                selected(roleManager, event)
            }
            "2️⃣" -> {
                roleManager.addRole(guild, user!!.id, grade2!!)
                selected(roleManager, event)
            }
            "3️⃣" -> {
                roleManager.addRole(guild, user!!.id, grade3!!)
                selected(roleManager, event)
            }
            "4️⃣" -> {
                roleManager.addRole(guild, user!!.id, grade4!!)
                selected(roleManager, event)
            }
        }
    }

    private fun selected(roleManager: RoleManager, event: MessageReactionAddEvent) {
        if (!first) return

        roleManager.deleteLatestMessage(event.channel)
        RoleMessage().send(event.user!!)
        event.jda.removeEventListener(this)

        first = false
    }
}