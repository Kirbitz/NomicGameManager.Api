package nomic.api

import nomic.api.models.ResponseFormat
import nomic.domain.entities.EndUser
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * This controller listens on `api/game` and works to provide, remove, or manipulate data related to games in the database
 *
 * @see[nomic.domain.games.GameDomain]
 * @param[gameDomain] This dependency is a domain service used by the endpoint to manipulate data in the database related to games.
 */
@RestController
@RequestMapping("api/game")
class GamesEndpoint(
    val gameDomain: GameDomain,
    val errorParser: ErrorParser
) {

    /**
     * This endpoint listens on `api/game/create` and takes in data about a game that will be inserted into the game table in the DB
     *
     * @param[input] The game data to eventually insert
     * @return A spring entity representing the response that gets serialized into JSON
     */
    @PostMapping("create")
    fun createGame(@RequestBody input: GameModel): ResponseEntity<ResponseFormat<String>> {
        gameDomain.createGame(input)
        return ResponseEntity(ResponseFormat(true, HttpStatus.CREATED, "Game Created"), HttpStatus.CREATED)
    }

    /**
     * This endpoint listens on `api/game/list`, allowing callers to paginate through the list
     * of games that the authenticated user has created. It accepts a size and an optional offset.
     *
     * @param[size] The size of the batch of games to retrieve. Maximum size is 100
     * games, and the default is 100.
     * @param[offset] How much the batch should be offset. This is used for paginatin
     * games, and the default to 0.
     * @param[user] The user whose games will be listed, injected by the authentication framework.
     * @return A Spring entity representing the response that gets serialized into JSON
     */
    @GetMapping("list")
    fun listGames(
        @RequestParam(defaultValue = "100") size: UInt,
        @RequestParam(defaultValue = "0") offset: UInt,
        @AuthenticationPrincipal authUser: EndUser
    ): ResponseEntity<*> {
        if (0U >= size || 100U < size) {
            return errorParser.fromBadArguments("You must request at least 1 game and at most 100 games.")
        }

        val games = gameDomain.listGames(authUser, size, offset)

        if (games.isEmpty) {
            return errorParser.fromNotFound("Invalid offset")
        } else {
            return ResponseEntity(
                ResponseFormat(
                    true,
                    HttpStatus.OK,
                    games.get()
                ),
                HttpStatus.OK
            )
        }
    }

    /**
     * This endpoint listens on `api/game/remove/ExistingGameId` and takes in a gameId that will be deleted from the DB
     *
     * @param[gameId] The game id to be deleted from the database
     * @return A spring entity representing the response that gets serialized into JSON
     */
    @DeleteMapping("remove/{gameid}", produces = ["application/json;charset=UTF-8"])
    fun deleteGame(@PathVariable(value = "gameid") gameId: String): ResponseEntity<ResponseFormat<String>> {
        gameDomain.deleteGame(gameId)

        // Return the response object
        return ResponseEntity(ResponseFormat(true, HttpStatus.ACCEPTED, "Game Deleted"), HttpStatus.ACCEPTED)
    }
}
