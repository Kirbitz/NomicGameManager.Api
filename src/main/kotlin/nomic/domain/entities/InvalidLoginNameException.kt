package nomic.domain.entities

/**
 * This exception is thrown when an invalid [LoginName] is constructed which violate the business rules of logins containing only
 * alphanumeric characters, dashes, and underscores.
 */
class InvalidLoginNameException(name: String) : Exception(
    "Invalid login name \'$name\': Login names must contain only alphanumeric characters, dashes, or underscores."
)
