package nomic.api.models

import org.springframework.http.HttpStatus

/**
 * Raw Representation of Data
 *
 * @property success a bool flag indicating if the request worked
 * @property status the http status code for the request
 * @property data information related to the request
 */
data class ResponseFormat<T>(
    val success: Boolean,
    val status: HttpStatus,
    val data: T
)
