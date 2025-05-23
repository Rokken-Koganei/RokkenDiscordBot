package com.rokken.data

class DataManager {
    companion object {
        private var BOT_TOKEN = ""
        private var SERVER_ID = ""
        private var DATABASE_URL = ""
        private var DATABASE_NAME = ""
        private var DATABASE_USERNAME = ""
        private var DATABASE_PASSWORD = ""

        fun initializeEnv() {
            BOT_TOKEN = System.getenv("BOT_TOKEN")
            SERVER_ID = System.getenv("SERVER_ID")
            DATABASE_URL = System.getenv("DATABASE_URL")
            DATABASE_NAME = System.getenv("DATABASE_NAME")
            DATABASE_USERNAME = System.getenv("DATABASE_USER")
            DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD")
        }

        fun initializeData(
            botToken: String,
            serverId: String,
            databaseUrl: String,
            databaseName: String,
            databaseUsername: String,
            databasePassword: String
        ) {
            BOT_TOKEN = botToken
            SERVER_ID = serverId
            DATABASE_URL = databaseUrl
            DATABASE_NAME = databaseName
            DATABASE_USERNAME = databaseUsername
            DATABASE_PASSWORD = databasePassword
        }

        fun getBotToken(): String {
            return BOT_TOKEN
        }

        fun getServerId(): String {
            return SERVER_ID
        }

        fun getDatabaseUrl(): String {
            return DATABASE_URL
        }

        fun getDatabaseName(): String {
            return DATABASE_NAME
        }

        fun getDatabaseUsername(): String {
            return DATABASE_USERNAME
        }

        fun getDatabasePassword(): String {
            return DATABASE_PASSWORD
        }
    }
}