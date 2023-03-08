package nomic.data.repositories

import nomic.domain.entities.IEntity
import java.util.Optional

/**
 * This interface specifies the generic repository pattern for domain entities on top of the data access layer
 * @param[TEntity] A generic type accepting any domain [nomic.domain.entities.Entity][Entity]
 */
interface IRepository<TEntity> where TEntity : IEntity {
    /**
     * Persists all changes to the provided entity to the underlying data layer
     *
     * @param[entity] The entity which has undergone changes
     */
    fun update(entity: TEntity)

    /**
     * Retrieves an entity with the provided id.
     *
     * @param[id] The unique integer id of the desired entity
     * @return An [java.util.Optional][Optional] wrapping the entity if successful, an empty optional otherwise
     */
    fun getById(id: Int): Optional<TEntity>

    /**
     * Deletes the provided entity from the underlying data layer
     *
     * @param[entity] The entity which is to be deleted
     */
    fun delete(entity: TEntity)
}
