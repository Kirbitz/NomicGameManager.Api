package nomic.domain.entities

/**
 * This domain entity represents a user.
 *
 * @property[name] The name of the user - has no business rules regarding valid names
 */
class EndUser(override val id: Int, var name: String) : IEntity
