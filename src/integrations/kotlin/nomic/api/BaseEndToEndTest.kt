package nomic.api

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class BaseEndToEndTest() {

    protected final fun <T> createRequest(body: T? = null): HttpEntity<T> {
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_JSON

        return HttpEntity(body, headers)
    }
}
