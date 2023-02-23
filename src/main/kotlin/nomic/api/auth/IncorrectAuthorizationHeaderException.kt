package nomic.api.auth

import nomic.api.BadRequestException

class IncorrectAuthorizationHeaderException(authType: String) : BadRequestException("Incorrect authorization header: expected Basic but got $authType")
