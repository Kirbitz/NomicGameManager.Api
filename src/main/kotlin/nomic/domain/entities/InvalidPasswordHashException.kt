package nomic.domain.entities

class InvalidPasswordHashException : Exception("Password hash must be stored as hash rather than plaintext.")
