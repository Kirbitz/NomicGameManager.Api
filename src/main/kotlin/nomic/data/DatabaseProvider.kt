package nomic.data

import nomic.DatabaseConfigProperties
import org.ktorm.database.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseProvider {

    @Bean
    fun getDatabase(props: DatabaseConfigProperties): Database {
        return Database.connect(
            url = "jdbc:mysql://${props.endpoint}/${props.schema}",
            user = props.username,
            password = props.password,
        )
    }
}
