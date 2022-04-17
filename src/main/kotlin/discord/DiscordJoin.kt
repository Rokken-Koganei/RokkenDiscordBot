package discord

import discord.reaction.FirstReactionListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color


class DiscordJoin : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val user = event.user
        user.openPrivateChannel().queue { channel ->
            channel.sendMessage("**__ロッ研オンライン部室へようこそ！__**\nこの部室は、交流の場を作りたいという願いのもと、作られました！\nこれから、いくつか質問に答えてくださると助かります！").queue()
            channel.sendMessageEmbeds(createEmbed()).queue {
                it.addReaction("U+2B55").queue()
                it.addReaction("U+274C").queue()
            }
            event.jda.addEventListener(FirstReactionListener())
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("現在部員ですか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        embed.addField("部員である。", ":o: を選択", false)
        embed.addField("部員ではないが興味がある。", ":x: を選択", false)

        return embed.build()
    }
}