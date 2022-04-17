package discord

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.system.exitProcess

class DiscordCommand : ListenerAdapter() {
    private val commandPrefix = "!"

    //メッセージ受け取った時に呼ばれるやつ
    override fun onMessageReceived(event: MessageReceivedEvent) {
        //打たれたメッセージ取得
        val msg = event.message
        //チャットが打たれたチャンネル取得
        val channel = event.channel

        when(msg.contentRaw){
            commandPrefix + "hello" -> { //!helloの時
                //メッセージ送信
                channel.sendMessage(event.member!!.effectiveName + "さん、こんにちは！").queue()

                val time = System.currentTimeMillis()
                channel.sendMessage(event.member.toString() + "さん、こんにちは！") /* => RestAction<Message> */
                    .queue { response: Message ->
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue()
                    }
            }

            commandPrefix + "exit" -> { //!exitの時
                channel.sendMessage("プログラムを終了します。").queue()
                exitProcess(0)
            }
        }

    }
}