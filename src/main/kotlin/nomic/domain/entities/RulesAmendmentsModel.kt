package nomic.domain.entities

/**
 * The rules and amendments object to be passed back to the caller of the endpoint
 *
 * @property ruleId the unique if for the rule
 * @property index the position of the rule within the context of a game
 * @property title the name of this rule
 * @property description the clarifying text for the rule
 * @property mutable flag for mutable/immutable rules
 * @property amendments the list of amendments that are attached to a rule
 * @see[nomic.domain.entities.AmendmentModel]
 */
data class RulesAmendmentsModel(
    val ruleId: Int,
    val index: Int,
    val title: String,
    val description: String? = null,
    val mutable: Boolean,
    var amendments: MutableList<AmendmentModel>? = mutableListOf()
)


data class RulesModel(
    val ruleId: Int,
    val index: Int,
    val title: String,
    val description: String? = null,
    val mutable: Boolean,
    val gameID: Int
)

/**
 * The amendments object to be passed back to the caller of the endpoint
 *
 * @property amendId the primary key for an amendment
 * @property index the position of an amendment under a rule
 * @property description the context of an amendment
 * @property title what an amendment is called
 * @property active flag for whether an amendment is still in effect
 */
data class AmendmentModel(
    val amendId: Int,
    val index: Int,
    val description: String,
    val title: String,
    val active: Boolean
)
