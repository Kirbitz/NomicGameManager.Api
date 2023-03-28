package nomic.api

import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.EndUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class BaseEndToEndTest(@Autowired val tokenRegistry: ITokenRegistry) {

    protected fun <T> createRequest(body: T? = null, user: EndUser? = null): HttpEntity<T> {
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_JSON

        if (user != null) {
            headers.setBearerAuth(tokenRegistry.issueToken(user))
        }

        return HttpEntity(body, headers)
    }
}
