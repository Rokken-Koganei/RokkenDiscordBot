package com.rokken.discord.message

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color

class IntentionMessage {
    fun send(user: User) {
        user.openPrivateChannel().queue { channel ->
            channel.sendMessage(
                "**__ロック研究会オンライン部室継続アンケート__**\n" +
                    "ロック研究会オンライン部室を利用していただいてありがとうございます！\n" +
                    "来年度も使用を継続するかどうかのアンケートとなります。\n" +
                    "2 週間回答がなければ自動キックします。再度加入希望の際は幹部にお問い合わせください。"
            ).queue()
            channel.sendMessageEmbeds(createEmbed()).queue {
                it.addReaction("⭕").queue()
            }
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("来年度も使用を継続しますか？")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        embed.addField("する！！！", ":o: を選択", false)
        embed.addField("しない...", "無視を選択", false)

        return embed.build()
    }
}