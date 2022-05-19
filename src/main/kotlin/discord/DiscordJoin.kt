package discord

import discord.message.FirstMessage
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class DiscordJoin : ListenerAdapter() {
    companion object {
        lateinit var joinedGuild: Guild
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val user = event.user
        joinedGuild = event.guild

        FirstMessage().send(user)
    }
}