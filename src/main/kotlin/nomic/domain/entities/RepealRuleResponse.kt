package nomic.domain.entities

data class RepealRuleResponse(
        val status: Boolean,
        val message: String? = null,
        val ruleId: Int
)
