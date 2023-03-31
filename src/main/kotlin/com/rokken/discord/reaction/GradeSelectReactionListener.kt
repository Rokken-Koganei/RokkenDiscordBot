package com.rokken.discord.reaction

import com.rokken.discord.DiscordMain
import com.rokken.discord.role.RoleManager
import com.rokken.discord.message.RoleMessage
import com.rokken.discord.role.GradeRole
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GradeSelectReactionListener: ListenerAdapter() {
    companion object {
        val queue = ArrayList<User>()
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        val isBot = user?.isBot ?: true // null check
        if (isBot) return
        // キューの中にそのユーザーがいるかどうか
        if (!queue.contains(user)) return

        val guild = DiscordMain.rokkenGuild
        val roleManager = RoleManager()

        // 学年に適した期をロールとして与える
        val gr = GradeRole()

        when (event.reactionEmote.name) {
            "1️⃣" -> {
                roleManager.addRole(guild, user!!.id, gr.getTermRole(1))
                selected(roleManager, event)
            }
            "2️⃣" -> {
                roleManager.addRole(guild, user!!.id, gr.getTermRole(2))
                selected(roleManager, event)
            }
            "3️⃣" -> {
                roleManager.addRole(guild, user!!.id, gr.getTermRole(3))
                selected(roleManager, event)
            }
            "4️⃣" -> {
                roleManager.addRole(guild, user!!.id, gr.getTermRole(4))
                selected(roleManager, event)
            }
        }
    }

    private fun selected(roleManager: RoleManager, event: MessageReactionAddEvent) {
        roleManager.deleteLatestMessage(event.channel)
        RoleMessage().send(event.user!!)

        val user = event.user!!
        queue.remove(user)
        RoleSelectReactionListener.queue.add(user)
    }
}