package nomic.data

/**
 * This exception is thrown when an id for an invalid [nomic.domain.entities.Entity][Entity] is provided to a [Repository]
 * to act upon but does not exist in the database.
 */
class EntityNotFoundException(id: Int) : Exception("The entity with id $id was not found on the database.")
