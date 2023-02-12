package models

/**
 * Data Class for an Amendment
 *
 * @property ruleId the unique if for the rule
 * @property index the position of the rule within the context of a game
 * @property title the name of this rule
 * @property description the clarifying text for the rule
 * @property amendments the list of amendments connected to this rule
 */
data class Rule (
    val ruleId: Int,
    val index: Int,
    val title: String,
    val description: String,
    val amendments: List<Amendment>
)