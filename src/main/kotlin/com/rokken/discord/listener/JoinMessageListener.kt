package com.rokken.discord.listener

import com.rokken.discord.DiscordMain
import com.rokken.discord.role.GradeRole
import com.rokken.discord.role.RoleManager
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

class JoinMessageListener : ListenerAdapter() {
    private val gr = GradeRole()
    private val roleManager = RoleManager()
    private val queue = ArrayList<User>()

    private lateinit var user: User

    // ユーザーが参加したとき期を選択するメッセージを送信する
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        user = event.user
        val guild = event.guild
        val unanswered = guild.getRoleById(RoleManager.UNANSWERED)

        queue.add(user)
        roleManager.addRole(guild, event.member, unanswered!!)
        sendMessage()
    }

    // ユーザーがリアクションを選択したときに呼び出される
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        val isBot = user?.isBot ?: true // null check
        if (isBot) return

        // キューの中にそのユーザーがいるかどうか
        if (!queue.contains(user)) return

        val guild = DiscordMain.rokkenGuild

        val selectedGrade =
            when (event.reactionEmote.name) {
                "1️⃣" -> 1
                "2️⃣" -> 2
                "3️⃣" -> 3
                "4️⃣" -> 4
                else -> return
            }

        user?.id?.let { roleManager.addRole(guild, it, gr.getTermRole(selectedGrade)) }

        roleManager.deleteLatestMessage(event.channel)
        event.channel.sendMessage(
            "**__答えてくださってありがとうございました！__**\n__必ずサーバー内の <#965608973527035994> を読んでください！__\nそれでは、楽しんでください！"
        ).queue()

        guild.getRoleById(RoleManager.UNANSWERED)?.let { roleManager.delRole(guild, event.userId, it) }
        queue.remove(user)
    }

    private fun sendMessage() {
        // welcome msg
        user.openPrivateChannel().queue { channel ->
            RoleManager().deleteBotMessages(channel)
            channel.sendMessage(
                "**__ロッ研オンライン部室へようこそ！__**\n" +
                        "この部室は、交流の場を作りたいという願いのもと、作られました！\n" +
                        "これから出てくる、いくつか質問に答えてください！\n" +
                        "もしも、リアクションしても反応がなければ @幹部 に連絡しましょう！"
            ).queue()

            // 期を選択するメッセージ
            channel.sendMessageEmbeds(createEmbed()).queue {
                for (emoji in listOf("1️⃣", "2️⃣", "3️⃣", "4️⃣")) {
                    it.addReaction("emoji").queue()
                }
            }
        }
    }

    private fun createEmbed(): MessageEmbed {
        val embed = EmbedBuilder()

        embed.setColor(Color.GREEN)

        embed.setTitle("何年生ですか？(何期生ですか？)")
        embed.setDescription("リアクションをクリックまたはタップして、質問に答えてください！")

        val grades = mapOf(1 to "one", 2 to "two", 3 to "three", 4 to "four")
        for (grade in grades) {
            embed.addField("${grade.key}年生(<@&${gr.getTermRole(grade.key).id})", ":${grade.value}: を選択", true)
        }

        return embed.build()
    }
}