package nomic.data

/**
<<<<<<< HEAD
 * This exception is thrown when an id for an invalid [nomic.domain.entities.Entity][Entity] is provided to a [Repository]
 * to act upon but does not exist in the database.
 *
 * @param id the id that was not found
 * @return Exception the response for the user to know that the id they were looking for was not found
 */
class EntityNotFoundException(id: Int) : Exception("The entity with id $id was not found on the database.")
