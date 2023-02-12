package models

/**
 * Data Class for an Amendment
 *
 * @property amendId the unique if for the amendment
 * @property index the position of the amendment within the context of a rule
 * @property description the clarifying text for the amendment
 */
data class Amendment (
    val amendId: Int,
    val index: Int,
    val description: String
)