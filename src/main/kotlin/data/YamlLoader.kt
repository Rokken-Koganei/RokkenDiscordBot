package data

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

class YamlLoader {
    companion object {
        fun load(path: String) {
            val inputStream: InputStream = File(path).inputStream()

            val yaml = Yaml()
            val data = yaml.load<Map<String, Any>>(inputStream)

            val source = data["source"]!!.toString()

            if (source == "ENV") {
                DataManager.initializeEnv()
                return
            }

            val botToken = data["bot-token"]!!.toString()

            val databaseUrl = data["database-url"]!!.toString()
            val databaseName = data["database-name"]!!.toString()
            val databaseUser = data["database-user"]!!.toString()
            val databasePassword = data["database-password"]!!.toString()

            DataManager.initializeData(botToken, databaseUrl, databaseName, databaseUser, databasePassword)
        }
    }
}