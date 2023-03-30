package nomic.domain.entities

/**
 * This exception is thrown when an invalid [PasswordHash] is constructed. Valid hashes must use the Argon2 algorithm and
 * stored in the modular PHC format.
 */
class InvalidPasswordHashException : Exception("Password hash must be stored as hash rather than plaintext.")
