package nomic.data

class EntityNotFoundException(id: Int) : Exception("The entity with id $id was not found on the database.")
