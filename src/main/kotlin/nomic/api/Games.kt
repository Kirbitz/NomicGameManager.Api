package nomic.api

import nomic.data.EntityNotFoundException
import nomic.domain.games.GameDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/games")
class Games(val gameDomain: GameDomain) {
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @DeleteMapping("remove/{gameid}", produces = ["application/json;charset=UTF-8"])
    fun deleteGame(@PathVariable(value = "gameid") gameId: String): ResponseEntity<Any> {
        gameDomain.deleteGame(gameId)

        // Return the response object
        return ResponseEntity("Game Deleted", HttpStatus.ACCEPTED)
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
