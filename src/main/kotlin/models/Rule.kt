package models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Data Class for an Amendment
 *
 * @property ruleId the unique if for the rule
 * @property index the position of the rule within the context of a game
 * @property title the name of this rule
 * @property description the clarifying text for the rule
 * @property mutable flag for mutable/immutable rules
 */
interface Rule : Entity<Rule> {
    companion object : Entity.Factory<Rule>()
    val ruleId: Int
    val index: Int
    val title: String
    val description: String
    val mutable: Boolean
}

object Rules : Table<Rule>("Rule") {
    val ruleId = int("ruleId").primaryKey().bindTo {it.ruleId}
    val index = int("index").bindTo {it.index}
    val title = varchar("title").bindTo {it.title}
    val description = varchar("description").bindTo {it.description}
    val mutable = boolean("mutable").bindTo {it.mutable}
}