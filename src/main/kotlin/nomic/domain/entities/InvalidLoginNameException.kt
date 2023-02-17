package nomic.domain.entities

class InvalidLoginNameException(name: String) : Exception(
    "Invalid login name \'$name\': Login names must contain only alphanumeric characters, dashes, or underscores."
)
