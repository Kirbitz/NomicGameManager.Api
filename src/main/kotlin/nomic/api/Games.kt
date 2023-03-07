package nomic.api

import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/game")
class Games(val gameDomain: GameDomain) {
    @PostMapping("create")
    fun createGame(@RequestBody input: GameModel): ResponseEntity<Any> {
        gameDomain.createGame(input)
        return ResponseEntity("Game Created", HttpStatus.CREATED)
    }
}
