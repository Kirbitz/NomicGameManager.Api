package nomic.data

import nomic.domain.ISpecification
import org.ktorm.entity.EntitySequence
import org.ktorm.schema.BaseTable

/**
 * This implements the specification with a strong coupling to KTorm. This is generically used
 * with references to the KTorm entity and its respective table.
 *
 * While the strong coupling to Ktorm is very unfortunate, there aren't any readily
 * apparent and easy-to-implement alternatives that can translate an expression with
 * domain layer entities into an expression with database DTOs that Ktorm can easily
 * parse into the corresponding SQL.
 *
 * This coupling is partially alleviated with specification factories.
 *
 * @param[E] The Ktorm entity being operated on
 * @param[T] The Ktorm table entity of [E]
 */
fun interface IDatabaseSpecification<E, T> : ISpecification where E : Any, T : BaseTable<E> {
    fun apply(sequence: EntitySequence<E, T>): EntitySequence<E, T>
}
