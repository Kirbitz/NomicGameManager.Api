package nomic.domain.entities

data class RepealRuleResponse(
        val success: Boolean,
        val message: String? = null,
        val ruleId: Int
)
