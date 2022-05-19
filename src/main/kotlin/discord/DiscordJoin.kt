package discord

import discord.message.FirstMessage
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordJoin : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val user = event.user

        FirstMessage().send(user)
    }
}