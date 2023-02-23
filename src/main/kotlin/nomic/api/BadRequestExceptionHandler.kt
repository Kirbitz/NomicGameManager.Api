package nomic.api

import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class BadRequestExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleConflict(ex: Exception): ResponseEntity<Any>? {
        // TODO Refactor?
        // Bad Conversion
        if (ex is MethodArgumentTypeMismatchException) {
            if (ex.cause != null && ex.cause !is ConversionFailedException) {
                throw UnsupportedOperationException("Unexpected and unsupported exception found", ex.cause)
            }

            return ResponseEntity.badRequest().body(ex.cause!!.cause?.message)
        }

        return ResponseEntity.badRequest().body(ex.message)
    }
}
