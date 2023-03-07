package nomic.api

import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/game")
class GamesEndpoint(val gameDomain: GameDomain) {
    // Path to endpoint is api/game/create
    @PostMapping("create")
    fun createGame(@RequestBody input: GameModel): ResponseEntity<Any> {
        gameDomain.createGame(input)
        return ResponseEntity("Game Created", HttpStatus.CREATED)
    }

    // Path to endpoint is api/game/remove/ExistingGameId
    @DeleteMapping("remove/{gameid}", produces = ["application/json;charset=UTF-8"])
    fun deleteGame(@PathVariable(value = "gameid") gameId: String): ResponseEntity<Any> {
        gameDomain.deleteGame(gameId)

        // Return the response object
        return ResponseEntity("Game Deleted", HttpStatus.ACCEPTED)
    }
}
