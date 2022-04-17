package data

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

class YamlLoader {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

        fun load(path: String) {
            logger.info("Loading yaml file: $path...")
            val inputStream: InputStream = File(path).inputStream()

            val yaml = Yaml()
            val data = yaml.load<Map<String, Any>>(inputStream)
            logger.info("Yaml file loaded: $path!")

            val source = data["source"]!!.toString()

            if (source == "ENV") {
                logger.info("Loading ENV variables...")
                DataManager.initializeEnv()
                logger.info("ENV variables loaded!")
                return
            }

            logger.info("Loading data from yml...")
            val botToken = data["bot-token"]!!.toString()

            val databaseUrl = data["database-url"]!!.toString()
            val databaseName = data["database-name"]!!.toString()
            val databaseUser = data["database-user"]!!.toString()
            val databasePassword = data["database-password"]!!.toString()

            DataManager.initializeData(botToken, databaseUrl, databaseName, databaseUser, databasePassword)
            logger.info("Data loaded!")
        }
    }
}