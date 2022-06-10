package com.rokken.discord.command

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class CheckPermission {
    companion object {
        fun hasCommandPermission(event: SlashCommandInteractionEvent, roleStr: String): Boolean {
            // 鯖からのメッセージ以外は受け付けない
            if (!event.isFromGuild) return false

            val guild = event.guild
            val role = guild!!.getRoleById(roleStr)!!

            // ロールが幹部でなければ無視
            return event.member!!.roles.contains(role)
        }
    }
}