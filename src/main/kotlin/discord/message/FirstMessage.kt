package discord.message

import discord.RoleManager
import discord.reaction.FirstReactionListener
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class FirstMessage {
    fun send(user: User) {
        user.openPrivateChannel().queue { channel ->
            RoleManager().deleteBotMessages(channel)
            channel.sendMessage("**__ロッ研オンライン部室へようこそ！__**\nこの部室は、交流の場を作りたいという願いのもと、作られました！\nこれから、いくつか質問に答えてくださると助かります！").queue()
            channel.sendMessageEmbeds(createEmbed()).queue {
                it.addReaction("⭕").queue()
                it.addReaction("❌").queue()
                it.jda.addEventListener(FirstReactionListener())
            }
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("部員届をすでに提出済みですか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        embed.addField("提出済みである (部員)", ":o: を選択", false)
        embed.addField("提出済みでない (新歓等)", ":x: を選択", false)

        return embed.build()
    }
}