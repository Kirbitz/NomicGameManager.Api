package nomic.api

import nomic.api.models.ResponseFormat
import nomic.domain.auth.ITokenRegistry
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class ListGamesTests(
    @Autowired val client: TestRestTemplate,
    @Autowired tokenRegistry: ITokenRegistry
) : BaseEndToEndTest(tokenRegistry) {

    @Test
    fun test_listGames_incompleteSize_noOffset() {
        val size = 100U
        val offset = 0U
        val entity = client.exchange<ResponseFormat<List<GameModel>>>(
            "/api/game/list?size=$size&offset=$offset",
            HttpMethod.GET,
            createRequest<Any>(user = EndUser(1610, "ListGamesUser1"))
        )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data).isNotNull

        val data = entity.body?.data!!
        Assertions.assertThat(data.size).isEqualTo(4)
    }

    @Test
    fun test_listGames_completeSize_offset() {
        val size = 2U
        val offset = 1U
        val entity = client.exchange<ResponseFormat<List<GameModel>>>(
            "/api/game/list?size=$size&offset=$offset",
            HttpMethod.GET,
            createRequest<Any>(user = EndUser(1620, "ListGamesUser2"))
        )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data).isNotNull

        val data = entity.body?.data!!
        Assertions.assertThat(data.size).isEqualTo(2)

        Assertions.assertThat(data[0].gameId).isEqualTo(1622)
        Assertions.assertThat(data[1].gameId).isEqualTo(1621)
    }

    @Test
    fun test_listGames_invalidOffset() {
        val size = 2U
        val offset = 10U
        val entity = client.exchange<ResponseFormat<String>>(
            "/api/game/list?size=$size&offset=$offset",
            HttpMethod.GET,
            createRequest<Any>(user = EndUser(1620, "ListGamesUser2"))
        )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.NOT_FOUND)

        // Case-insensitive of starting letter
        Assertions.assertThat(entity.body?.data).contains("ffset")
    }

    @Test
    fun test_listGames_tooSmallSize() {
        val size = 0U
        val offset = 0U
        val entity = client.exchange<ResponseFormat<String>>(
            "/api/game/list?size=$size&offset=$offset",
            HttpMethod.GET,
            createRequest<Any>(user = EndUser(1620, "ListGamesUser2"))
        )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data).contains("1")
    }

    @Test
    fun test_listGames_tooLargeSize() {
        val size = 1000U
        val offset = 0U
        val entity = client.exchange<ResponseFormat<String>>(
            "/api/game/list?size=$size&offset=$offset",
            HttpMethod.GET,
            createRequest<Any>(user = EndUser(1620, "ListGamesUser2"))
        )

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        Assertions.assertThat(entity.body?.success).isFalse
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(entity.body?.data).contains("100")
    }

    @Test
    fun test_listGames_offsetOptional() {
        val request = createRequest<Any>(user = EndUser(1620, "ListGamesUser2"))
        val size = 100
        val entity = client.exchange<ResponseFormat<List<GameModel>>>("/api/game/list?size=$size", HttpMethod.GET, request)

        Assertions.assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        Assertions.assertThat(entity.body?.success).isTrue
        Assertions.assertThat(entity.body?.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(entity.body?.data).isNotNull()
        Assertions.assertThat(entity.body?.data!!.size).isEqualTo(3)
    }
}
