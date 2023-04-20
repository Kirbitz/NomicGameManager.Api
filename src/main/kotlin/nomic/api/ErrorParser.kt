package nomic.api

import nomic.api.models.ResponseFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ErrorParser {

    /**
     * Constructs an API response to bad arguments being passed
     *
     * @param[message] A detailed message explaining how the arguments passed were invalid
     * @return A response entity that can be immediately returned in a controller.
     */
    fun fromBadArguments(message: String): ResponseEntity<ResponseFormat<String>> {
        return ResponseEntity(
            ResponseFormat(
                false,
                HttpStatus.BAD_REQUEST,
                message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    /**
     * Constructs an API response to not being able to find an entity
     *
     * @param[message] A detailed message explaining what entity the API failed to locate
     * @return A response entity that can be immediately returned in a controller.
     */
    fun fromNotFound(message: String): ResponseEntity<ResponseFormat<String>> {
        return ResponseEntity(
            ResponseFormat(
                false,
                HttpStatus.NOT_FOUND,
                message
            ),
            HttpStatus.NOT_FOUND
        )
    }
}
