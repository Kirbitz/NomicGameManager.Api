package nomic.data

import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * This Spring [Configuration] initializes the KTorm [Database] to connect to the database using
 * the connection string and credentials as specified in [DatabaseConfigProperties] and readies it for dependency injection.
 *
 * @see[org.springframework.context.annotation.Configuration]
 * @see[org.ktorm.database.Database]
 * @see[DatabaseConfigProperties]
 */
@Configuration
class DatabaseProvider {

    /**
     * Constructs the KTorm [Database] using the settings from [props]
     *
     * @param[props] The Database Configuration properties, typically loaded from `secrets.properties`
     * @return A connected KTorm [Database]
     */
    @Bean
    fun getDatabase(props: DatabaseConfigProperties): Database {
        return Database.connect(
            url = props.connectionString,
            user = props.username,
            password = props.password
        )
    }
}
