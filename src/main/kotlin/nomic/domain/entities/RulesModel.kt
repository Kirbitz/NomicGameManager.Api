package nomic.domain.entities

data class RulesModel(
    val ruleId: Int,
    val index: Int,
    val title: String,
    val description: String? = null,
    val mutable: Boolean,
    val gameID: Int
)
