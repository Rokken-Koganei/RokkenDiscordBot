package com.rokken.discord

import com.rokken.Main
import com.rokken.discord.role.RoleManager
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

        fun run() {
            // ファイルあったらロード
            if (Files.exists(path)) {
                list = loadFile()
                return
            }

            val members = DiscordMain.rokkenGuild.findMembersWithRoles(DiscordMain.rokkenGuild.getRoleById(RoleManager.MEMBER))
            members.onSuccess {
                for (mem in it)
                    list.add(mem.id)
                writeFile()
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

        fun getList() : ArrayList<String> {
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
         * 移行時に使用するファイルをロードしてロード結果を返す
         * @return 読み込んだ結果のリスト
         */
        private fun loadFile() : ArrayList<String> {
            val loadList = ArrayList<String>()

            try {
                val line = Files.readAllLines(path, charset)
                for (str in line)
                    loadList.add(str)
            } catch (e: IOException) {
                logger.error(e.message)
            }

            return loadList
        }
    }
}