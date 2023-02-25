package nomic.data

/**
 * Exception thrown when entity is not found in database
 *
 * @param id the id that was not found
 * @return Exception the response for the user to know that the id they were looking for was not found
 */
class EntityNotFoundException(id: Int) : Exception("The entity with id $id was not found on the database.")
