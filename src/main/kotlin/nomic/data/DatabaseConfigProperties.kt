package nomic.data

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * This data class represents the database related configuration settings that are set in `secrets.properties` via Spring.
 *
 * @see[org.springframework.boot.context.properties.ConfigurationProperties]
 * @prop[connectionString] The full JDBC connection string, containing the url, port, JDBC driver, and database schema
 * @prop[username] The username used to connect to the database at the location specified in [connectionString]
 * @prop[password] The password used to connect to the database at the location specified in [connectionString]
 */
@ConfigurationProperties(prefix = "db")
data class DatabaseConfigProperties(
    var connectionString: String = "",
    var username: String = "",
    var password: String = ""
)
