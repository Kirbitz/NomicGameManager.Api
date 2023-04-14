package nomic.api

import nomic.api.models.ListGamesApiRequestModel
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
import org.springframework.web.bind.annotation.RestController

/**
 * This controller listens on `api/game` and works to provide, remove, or manipulate data related to games in the database
 *
 * @see[nomic.domain.games.GameDomain]
 * @param[gameDomain] This dependency is a domain service used by the endpoint to manipulate data in the database related to games.
 */
@RestController
@RequestMapping("api/game")
class GamesEndpoint(val gameDomain: GameDomain) {

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

    @GetMapping("list")
    fun listGames(
        @RequestBody input: ListGamesApiRequestModel,
        @AuthenticationPrincipal authUser: EndUser
    ): ResponseEntity<*> {
        if (0U >= input.size || 100U < input.size) {
            return ResponseEntity(
                ResponseFormat(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "You must request at least 1 game and at most 100 games."
                ),
                HttpStatus.BAD_REQUEST
            )
        }

        val games = gameDomain.listGames(authUser, input.size, input.offset)

        if (games.isEmpty) {
            return ResponseEntity(
                ResponseFormat(
                    false,
                    HttpStatus.BAD_REQUEST,
                    "Invalid offset"
                ),
                HttpStatus.BAD_REQUEST
            )
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
