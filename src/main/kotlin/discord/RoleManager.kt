package discord

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Role

class RoleManager {
    fun addRole(guild: Guild, user: String, role: Role) {
        guild.addRoleToMember(user, role).queue()
    }

    fun deleteMessage(message: Message) {
        message.delete().queue()
    }
}