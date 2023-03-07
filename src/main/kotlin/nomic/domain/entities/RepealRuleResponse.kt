package nomic.domain.entities

/**
 * Raw Representation of Data
 *
 * @property success a bool flag indicating if the request worked
 * @property message a string that returns information related to the request
 * @property ruleId the id of a rule that is to be repealed
 */
data class RepealRuleResponse(
    val success: Boolean,
    val message: String,
    val ruleId: Int
)
