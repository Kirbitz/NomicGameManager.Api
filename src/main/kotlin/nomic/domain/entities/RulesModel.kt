package nomic.domain.entities

/**
 * Raw representation of data from the repository layer
 *
 * @property ruleId the unique if for the rule
 * @property index the position of the rule within the context of a game
 * @property title the name of this rule
 * @property description the clarifying text for the rule
 * @property mutable flag for mutable/immutable rules
 * @property gameID the unique id for active game
 * @see[nomic.domain.entities.RulesModel]
 */
data class RulesModel(
    val ruleId: Int,
    val index: Int,
    val title: String,
    val description: String? = null,
    val mutable: Boolean,
    val gameID: Int
)
