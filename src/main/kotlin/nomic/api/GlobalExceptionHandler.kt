package nomic.api

import nomic.api.models.ResponseFormat
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
class GlobalExceptionHandler(val errorParser: ErrorParser) : ResponseEntityExceptionHandler() {
    /**
     * Handles the Illegal Argument Exceptions that are thrown
     *
     * @return The response entity with error code and message
     */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<ResponseFormat<String>> {
        return errorParser.fromBadArguments(exception.message!!)
    }

    /**
     * Handles the Not Found Exceptions that are thrown
     *
     * @return The response entity with error code and message
     */
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException
    ): ResponseEntity<ResponseFormat<String>> {
        return errorParser.fromNotFound(exception.message!!)
    }

    /**
     * Handles any generic Exceptions that are thrown
     *
     * @return The response entity with error code and message
     */
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllUncaughtException(
        exception: Exception
    ): ResponseEntity<Any> {
        return ResponseEntity(ResponseFormat(false, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
