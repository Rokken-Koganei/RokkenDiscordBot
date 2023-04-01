package com.rokken.discord.reaction

import com.rokken.discord.DiscordMigration
import com.rokken.discord.role.RoleManager
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class IntentionReactionListener : ListenerAdapter()  {
    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        val user = event.user

        // bot が追加してたら何もしない
        val isBot = user?.isBot ?: true // null check
        if (isBot) return

        // 別のリスナに登録されていたら何もしない
        if (MemberSelectReactionListener.queue.contains(user) || GradeSelectReactionListener.queue.contains(user)) return

        when (event.reactionEmote.name) {
            "⭕" -> {
                DiscordMigration.updateFile(event.userId)
                for (i in 0..1)
                    RoleManager().deleteLatestMessage(event.channel)
                event.channel.sendMessage("継続が確定しました。回答ありがとうございます。").queue()
            }
        }
    }
}