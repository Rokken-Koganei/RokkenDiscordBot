package data

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.jar.JarFile

class ResourceExporter {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(ResourceExporter::class.java)

        /**
         * jar内ファイルを書き出す
         */
        fun export() {
            val file = File(Main.DIR_PATH)

            try {
                if (!file.exists()) {
                    logger.info("Exporting yml files...")

                    file.mkdir()
                    saveResource("data", file)

                    logger.info("Done.")
                }
            } catch (e: IOException) {
                logger.error("Exception occurred at export section", e)
            }
        }

        private fun saveResource(copyResource: String, directory: File) {
            val jarFile = File(ResourceExporter::class.java.protectionDomain.codeSource.location.path)
            if (jarFile.isFile) {
                // JARで実行する場合
                val jar = JarFile(jarFile)
                val entries = jar.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.startsWith("$copyResource/") && !entry.isDirectory) {
                        val dest = File(directory, entry.name.substring(copyResource.length + 1))
                        val parent = dest.parentFile
                        parent?.mkdirs()
                        val out = FileOutputStream(dest)
                        val `in` = jar.getInputStream(entry)
                        try {
                            val buffer = ByteArray(8 * 1024)
                            var s = 0
                            while (`in`.read(buffer).also { s = it } > 0) {
                                out.write(buffer, 0, s)
                            }
                        } finally {
                            `in`.close()
                            out.close()
                        }
                    }
                }
                jar.close()
            } else {
                // IDEで実行する場合
                val resource = File(ResourceExporter::class.java.classLoader.getResource(copyResource)!!.toURI())
                FileUtils.copyDirectory(resource, directory)
            }
        }
    }
}