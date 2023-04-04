package nomic.domain.entities

/**
 * Data class for amendment
 *
 * @property ruleId id of the rule amendment is associated with
 * @property id unique id for the amendment
 * @property index unique index created for the amendment
 * @property title the name of the amendment
 * @property description the clarifying text for amendment
 */
data class AmendmentInputModel(
    val ruleId: Int,
    val id: Int,
    val index: Int,
    val title: String,
    val description: String? = null
)
