package nomic.data

import nomic.NomicConfigProperties
import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseProvider {

    @Bean
    fun getDatabase(props: NomicConfigProperties): Database {
        return Database.connect(
            url = "jdbc:mysql://${props.dbEndpoint}:${props.dbPort}/${props.dbName}",
            user = props.dbUsername,
            password = props.dbPassword,
        )
    }
}
