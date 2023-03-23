package nomic.api.models

/**
 * Response format for exception that are thrown
 *
 * @property error flag for the occurrence of an error
 * @property msg the message to pass back to the user
 */
data class ExceptionResponseFormat(
    val error: Boolean,
    val msg: String?
)
