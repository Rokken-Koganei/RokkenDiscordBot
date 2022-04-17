package data

class DataManager {
    companion object {
        private lateinit var BOT_TOKEN: String
        private lateinit var DATABASE_URL: String
        private lateinit var DATABASE_NAME: String
        private lateinit var DATABASE_USERNAME: String
        private lateinit var DATABASE_PASSWORD: String

        fun initializeEnv() {
            BOT_TOKEN = System.getenv("BOT_TOKEN")
            DATABASE_URL = System.getenv("DATABASE_URL")
            DATABASE_NAME = System.getenv("DATABASE_NAME")
            DATABASE_USERNAME = System.getenv("DATABASE_USERNAME")
            DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD")
        }

        fun initializeData(
            botToken: String,
            databaseUrl: String,
            databaseName: String,
            databaseUsername: String,
            databasePassword: String
        ) {
            BOT_TOKEN = botToken
            DATABASE_URL = databaseUrl
            DATABASE_NAME = databaseName
            DATABASE_USERNAME = databaseUsername
            DATABASE_PASSWORD = databasePassword
        }

        fun getBotToken(): String {
            return BOT_TOKEN
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