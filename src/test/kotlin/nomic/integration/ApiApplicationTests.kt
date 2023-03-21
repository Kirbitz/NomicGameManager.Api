package nomic.integration

import nomic.main
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApiApplicationTests {
    @Test
    fun contextLoads() {
        main(arrayOf<String>())
    }
}
