package game.manager.nomic.api.config

import org.ktorm.database.Database

class DatabaseConfig(val nomicConfig: NomicConfigProperties) {
    fun connectDB(): Database {
        return Database.connect(
            url = "jdbc:mysql://${nomicConfig.dbEndpoint}/${nomicConfig.dbName}",
            user = nomicConfig.dbUsername,
            password = nomicConfig.dbPassword
        )
    }
}