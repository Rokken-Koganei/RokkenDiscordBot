package discord.message

import discord.reaction.RoleSelectReactionListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class RoleMessage {
    fun send(user: User) {
        user.openPrivateChannel().queue { channel ->
            channel.sendMessageEmbeds(createEmbed()).queue {
                it.addReaction("🎤").queue()
                it.addReaction("🎸").queue()
                it.addReaction("🥁").queue()
                it.addReaction("🪕").queue()
                it.addReaction("🎹").queue()
                it.addReaction("❌").queue()
                it.jda.addEventListener(RoleSelectReactionListener())
            }
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("希望楽器は何ですか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！\n選択が終了したら、:x: を選択してください。")

        embed.addField("Vo.", ":microphone: を選択", true)
        embed.addField("Gt.", ":guitar: を選択", true)
        embed.addField("Dr.", ":drum: を選択", true)
        embed.addField("Ba.", ":banjo: を選択", true)
        embed.addField("Key.", ":musical_keyboard: を選択", true)
        embed.addField("終了", ":x: を選択", true)

        return embed.build()
    }
}