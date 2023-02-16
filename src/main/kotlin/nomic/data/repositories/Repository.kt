package nomic.data.repositories

import nomic.domain.entities.Entity

interface Repository<TEntity> where TEntity : Entity {
    fun create(entity : TEntity)
    fun update(entity: TEntity)
    fun getById(id: Int) : TEntity
    fun delete(entity: TEntity)
}