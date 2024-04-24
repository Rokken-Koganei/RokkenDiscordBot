package com.rokken.discord

import com.rokken.Main
import com.rokken.discord.role.RoleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class DiscordMigration {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)
        private val path = Paths.get(Main.DIR_PATH, "migration.txt")
        private val charset = Charset.forName("UTF-8")
        private var list = ArrayList<String>()

//        async fun を作りたい
//        fun run() {
//            GlobalScope.launch {
//                // ファイルあったらロード
//
//                // 無かったら作成
//
//                // ロード
//            }
//        }


        fun run() {
            // ファイルあったらロード
            if (Files.exists(path)) {
                loadFile()
                return
            }

            val job = CoroutineScope(Dispatchers.Default).launch {
                // 無かったら作成
                val members = DiscordMain.rokkenGuild.findMembersWithRoles(DiscordMain.rokkenGuild.getRoleById(RoleManager.MEMBER))
                members.onSuccess {
                    for (mem in it)
                        list.add(mem.id)
                    writeFile()
                }
            }

            // TODO コルーチンごり押しすぎる
            while (!job.isCompleted) {
                Thread.sleep(100)
            }
        }

        /**
         * 移行時にリアクションした人をファイルから消す
         * @param reactedId リアクションした人の ID
         */
        fun updateFile(reactedId: String) {
            if (list.remove(reactedId))
                writeFile()
        }

        fun deleteFile() {
            if (Files.exists(path))
                Files.delete(path)
        }

        fun getList() : ArrayList<String> {
            if (list.isEmpty()) loadFile()
            return list
        }

        /**
         * 移行時に使用するファイルに書き込みを行う
         */
        private fun writeFile() {
            if (list.size <= 0) loadFile()
            try {
                if (!Files.exists(path))
                    Files.createFile(path)
                Files.write(path, list, charset, StandardOpenOption.TRUNCATE_EXISTING)
            } catch (e: IOException) {
                logger.error(e.message)
            }
        }

        /**
         * 移行時に使用するファイルがあればロードする
         */
        fun loadFile() {
            val loadList = ArrayList<String>()

            try {
                // ファイルが無かったら処理しない
                if (!Files.exists(path))
                    return

                // ファイル読み込み
                val line = Files.readAllLines(path, charset)
                for (str in line)
                    loadList.add(str)
                logger.info("$path is loaded.")
            } catch (e: IOException) {
                logger.error(e.message)
            }
            list = loadList
        }
    }
}