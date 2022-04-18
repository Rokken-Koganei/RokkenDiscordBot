package discord.reaction

import discord.DiscordJoin
import discord.RoleManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class FirstReactionListener : ListenerAdapter()  {
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return

        val guild = DiscordJoin.joinedGuild
        val roleManager = RoleManager()

        // 仮入部
        val role = guild.getRoleById("965268862016835594")

        val message = event.channel.retrieveMessageById(event.messageId).complete()

        var selected = false

        if (event.reactionEmote.name == "⭕") { // o
            roleManager.deleteMessage(message)
            selected = true
        } else if (event.reactionEmote.name == "❌") { // x
            roleManager.deleteMessage(message)
            RoleManager().addRole(guild, user!!.id, role!!)
            selected = true
        }

        if (selected) {
            event.channel.let { channel ->
                channel.sendMessageEmbeds(createEmbed()).queue {
                    it.addReaction("1️⃣").queue()
                    it.addReaction("2️⃣").queue()
                    it.addReaction("3️⃣").queue()
                    it.addReaction("4️⃣").queue()
                    it.jda.addEventListener(SecondReactionListener())
                }
            }
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("何年生ですか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        embed.addField("1年生", ":one: を選択", true)
        embed.addField("2年生", ":two: を選択", true)
        embed.addField("3年生", ":three: を選択", true)
        embed.addField("4年生", ":four: を選択", true)

        return embed.build()
    }
}