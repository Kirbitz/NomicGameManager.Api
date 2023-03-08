package nomic.domain.entities

/**
 * This domain entity represents a user.
 *
 * @property[name] The name of the user - has no business rules regarding valid names
 */
class User(override val id: Int, val name: String) : IEntity
