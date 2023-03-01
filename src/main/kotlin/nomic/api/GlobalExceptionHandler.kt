package nomic.api

import nomic.data.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * This handles all exceptions that could occur within an API endpoint
 */
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * Handles the Illegal Argument Exceptions that are thrown
     */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.BAD_REQUEST)
    }
    /**
     * Handles the Not Found Exceptions that are thrown
     */
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.NOT_FOUND)
    }

    /**
     * Handles any generic Exceptions that are thrown
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllUncaughtException(
        exception: Exception
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
