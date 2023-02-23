package nomic.data.dtos

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * Interface for Rule Table
 *
 * @property ruleId the unique if for the rule
 * @property index the position of the rule within the context of a game
 * @property title the name of this rule
 * @property description the clarifying text for the rule
 * @property mutable flag for mutable/immutable rules
 */
interface RuleDTO : Entity<RuleDTO> {
    companion object : Entity.Factory<RuleDTO>()
    val ruleId: Int
    val index: Int
    val title: String
    val description: String
    val mutable: Boolean
    val gameId: Int
}

object Rules : Table<RuleDTO>("Rule") {
    val ruleId = int("ruleId").primaryKey().bindTo { it.ruleId }
    val index = int("index").bindTo { it.index }
    val title = varchar("title").bindTo { it.title }
    val description = varchar("description").bindTo { it.description }
    val mutable = boolean("mutable").bindTo { it.mutable }
    val gameId = int("gameId").bindTo { it.gameId }
}