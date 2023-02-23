package nomic.api.auth

import nomic.api.BadRequestException

class BadAuthorizationHeaderException(message: String = "") : BadRequestException(
    "Bad authorization header passed" + (if (message.length > 0) ": $message" else "")
)
