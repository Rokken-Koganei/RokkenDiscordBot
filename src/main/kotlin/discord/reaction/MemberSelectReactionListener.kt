package discord.reaction

import discord.DiscordMain
import discord.RoleManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class MemberSelectReactionListener : ListenerAdapter()  {
    companion object {
        val queue = ArrayList<User>()
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return
        // キューの中にそのユーザーがいるかどうか
        if (!queue.contains(user)) return

        val guild = DiscordMain.rokkenGuild
        val roleManager = RoleManager()

        val member = guild.getRoleById(RoleManager.MEMBER)
        val preMember = guild.getRoleById(RoleManager.PRE_MEMBER)

        when (event.reactionEmote.name) {
            "⭕" -> {
                RoleManager().addRole(guild, user!!.id, member!!)
                selected(roleManager, event)
            }
            "❌" -> {
                RoleManager().addRole(guild, user!!.id, preMember!!)
                selected(roleManager, event)
            }
        }
    }

    private fun selected(roleManager: RoleManager, event: MessageReactionAddEvent) {
        roleManager.deleteLatestMessage(event.channel)
        event.channel.sendMessageEmbeds(createEmbed()).queue {
            it.addReaction("1️⃣").queue()
            it.addReaction("2️⃣").queue()
            it.addReaction("3️⃣").queue()
            it.addReaction("4️⃣").queue()
        }
        val user = event.user!!
        queue.remove(user)
        GradeSelectReactionListener.queue.add(user)
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