package discord.reaction

import discord.DiscordJoin
import discord.RoleManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class SecondReactionListener: ListenerAdapter() {
    private var first = true

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        if (event.user!!.isBot) return

        val guild = DiscordJoin.joinedGuild
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
        event.channel.sendMessageEmbeds(createEmbed()).queue {
            it.addReaction("🎤").queue()
            it.addReaction("🎸").queue()
            it.addReaction("🥁").queue()
            it.addReaction("🪕").queue()
            it.addReaction("🎹").queue()
            it.jda.addEventListener(ThirdReactionListener())
            it.jda.removeEventListener(this)
        }

        first = false
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("希望楽器は何ですか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        embed.addField("Vo.", ":microphone: を選択", true)
        embed.addField("Gt.", ":guitar: を選択", true)
        embed.addField("Dr.", ":drum: を選択", true)
        embed.addField("Ba.", ":banjo: を選択", true)
        embed.addField("Key.", ":musical_keyboard: を選択", true)

        return embed.build()
    }
}