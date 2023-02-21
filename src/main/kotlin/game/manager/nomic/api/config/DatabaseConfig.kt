package game.manager.nomic.api.config

import org.ktorm.database.Database
import kotlin.jvm.Throws

class DatabaseConfig(private val nomicConfig: NomicConfigProperties) {
    @Throws(Exception::class)
    fun connectDB(): Database {
        return Database.connect(
            url = "jdbc:mysql://${nomicConfig.dbEndpoint}/${nomicConfig.dbName}",
            user = nomicConfig.dbUsername,
            password = nomicConfig.dbPassword
        )
    }
}