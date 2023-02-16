package nomic.data.repositories

import nomic.domain.entities.Entity
import java.util.Optional

interface Repository<TEntity> where TEntity : Entity {
    fun update(entity: TEntity)
    fun getById(id: Int) : Optional<TEntity>
    fun delete(entity: TEntity)
}