package com.rokken.discord

import com.rokken.discord.message.FirstMessage
import com.rokken.discord.role.RoleManager
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordJoin : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val user = event.user
        val guild = event.guild
        val unanswered = guild.getRoleById(RoleManager.UNANSWERED)

        RoleManager().addRole(guild, event.member, unanswered!!)
        FirstMessage().send(user)
    }
}