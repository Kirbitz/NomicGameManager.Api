package nomic.data.dtos

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Interface for Amendment Table
 *
 * @property amendId the primary key for an amendment
 * @property index the position of an amendment under a rule
 * @property description the context of an amendment
 * @property title what an amendment is called
 * @property ruleId the foreign key back to a rule
 * @property active flag for whether an amendment is still in effect
 */
interface Amendment : Entity<Amendment> {
    companion object : Entity.Factory<Amendment>()
    val amendId: Int
    val index: Int
    val description: String
    val title: String
    val ruleId: Int
    val active: Boolean
}

object Amendments : Table<Amendment>("Amendment") {
    val amendId = int("amendId").primaryKey().bindTo { it.amendId }
    val index = int("index").bindTo { it.index }
    val description = varchar("description").bindTo { it.description }
    val title = varchar("title").bindTo { it.title }
    val ruleId = int("ruleId").bindTo { it.ruleId }
    val active = boolean("active").bindTo { it.active }
}