package nomic.data.dtos

import nomic.data.dtos.Rules.gameId
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
 * @property active flag to determine amendment visibility to the user
 * @property gameId The foreign key to the Games table
 */
interface RuleDTO : Entity<RuleDTO> {
    companion object : Entity.Factory<RuleDTO>()
    val ruleId: Int
    val index: Int
    val title: String
    val description: String
    val mutable: Boolean
    val active: Boolean
    val gameId: Int
}

/**
 * This object represents the credentials table in the database, employing KTorm's framework.
 *
 * @see[org.ktorm.schema.Table]
 * @property ruleId The primary key for the [Rules] table
 * @property index The position of a rule attached to a rule
 * @property title What the rule is called
 * @property description The specific details of a rule
 * @property mutable flag to determine whether a rule can be amended or deleted
 * @property active flag to determine rule visibility to the user
 * @property gameId The foreign key to the Games table
 */
object Rules : Table<RuleDTO>("Rule") {
    val ruleId = int("ruleId").primaryKey().bindTo { it.ruleId }
    val index = int("index").bindTo { it.index }
    val title = varchar("title").bindTo { it.title }
    val description = varchar("description").bindTo { it.description }
    val mutable = boolean("mutable").bindTo { it.mutable }
    val active = boolean("active").bindTo { it.active }
    val gameId = int("gameId").bindTo { it.gameId }
}
