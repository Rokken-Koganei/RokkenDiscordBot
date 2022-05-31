package discord

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.*

class RoleManager {
    companion object {
        const val UNANSWERED = "981120045608501249"
        const val MEMBER = "965964943616798740"
        const val PRE_MEMBER = "965268862016835594"

        const val GRADE_1 = "965235111731814420"
        const val GRADE_2 = "965235093348180039"
        const val GRADE_3 = "965235074813526056"
        const val GRADE_4 = "965235055142273094"

        const val VOCAL = "965235976114942034"
        const val GUITAR = "965235822800568320"
        const val DRUM = "965235713777995856"
        const val BASE = "965235743125536789"
        const val KEY = "965235681767096320"

        const val ADMIN = "965234118730342480"
    }

    fun addRole(guild: Guild, userStr: String, role: Role) {
        val user = guild.getMemberById(userStr)!!
        guild.addRoleToMember(user, role).queue()
    }

    fun addRole(guild: Guild, user: Member, role: Role) {
        guild.addRoleToMember(user, role).queue()
    }

    fun delRole(guild: Guild, userStr: String, role: Role) {
        val user = guild.getMemberById(userStr)!!
        guild.removeRoleFromMember(user, role).queue()
    }

    fun delRole(guild: Guild, user: Member, role: Role) {
        guild.removeRoleFromMember(user, role).queue()
    }

    fun delInstRole(guild: Guild, user: Member) {
        for (role in user.roles) {
            if (role.id == VOCAL || role.id == GUITAR || role.id == DRUM || role.id == BASE || role.id == KEY) {
                guild.removeRoleFromMember(user, role).queue()
            }
        }
    }

    fun deleteBotMessages(channel: MessageChannel) {
        runBlocking {
            val history = MessageHistory(channel)
            history.retrievePast(100).queue { messages ->
                messages.forEach {
                    if (it.author.isBot)
                        it.delete().queue()
                }
            }
        }
    }

    fun deleteLatestMessage(channel: MessageChannel) {
        val history = MessageHistory(channel)
        val msg = history.retrievePast(1).complete()

        channel.deleteMessageById(msg[0].id).complete()
    }
}