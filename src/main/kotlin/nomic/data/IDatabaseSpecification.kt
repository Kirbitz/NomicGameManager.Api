package nomic.data

import nomic.domain.ISpecification
import org.ktorm.entity.EntitySequence
import org.ktorm.schema.BaseTable

fun interface IDatabaseSpecification<E, T> : ISpecification where E : Any, T : BaseTable<E> {
    fun apply(sequence: EntitySequence<E, T>): EntitySequence<E, T>
}
