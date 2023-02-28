package nomic.domain.entities

/**
 * Raw representation of data from the repository layer
 *
 * @property ruleId the unique if for the rule
 * @property ruleIndex the position of the rule within the context of a game
 * @property ruleTitle the name of this rule
 * @property ruleDescription the clarifying text for the rule
 * @property ruleMutable flag for mutable/immutable rules
 * @property ruleActive flag to determine rule visibility to the user
 * @property amendId the primary key for an amendment
 * @property amendIndex the position of an amendment under a rule
 * @property amendDescription the context of an amendment
 * @property amendTitle what an amendment is called
 * @property amendActive flag for whether an amendment is still in effect
 * @see[nomic.domain.entities.RulesAmendmentsModel]
 */
data class RulesAmendmentsModel(
    val ruleId: Int,
    val ruleIndex: Int,
    val ruleTitle: String,
    val ruleDescription: String?,
    val ruleMutable: Boolean,
    val ruleActive: Boolean,
    val amendId: Int?,
    val amendIndex: Int?,
    val amendTitle: String?,
    val amendDescription: String?,
    val amendActive: Boolean?
)
