package nomic.api

import nomic.data.EntityNotFoundException
import nomic.domain.entities.GameModel
import nomic.domain.games.GameDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/game")
class Games(val gameDomain: GameDomain) {
    @PostMapping("create")
    fun createGame(@RequestBody input: GameModel): ResponseEntity<Any> {
        gameDomain.createGame(input)
        return ResponseEntity("Game Created", HttpStatus.CREATED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAllUncaughtException(
        exception: IllegalArgumentException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
