package nomic.data

import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseProvider {

    @Bean
    fun getDatabase(props: DatabaseConfigProperties): Database {
        return Database.connect(
            url = props.connectionString,
            user = props.username,
            password = props.password
        )
    }
}
