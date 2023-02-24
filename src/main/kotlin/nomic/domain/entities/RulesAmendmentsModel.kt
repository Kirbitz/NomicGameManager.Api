package nomic.domain.entities

data class RulesAmendmentsModel(
    val ruleId: Int? = null,
    val index: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val mutable: Boolean? = null,
    var amendments: MutableList<AmendmentModel>? = mutableListOf()
)

data class AmendmentModel(
    val amendId: Int?,
    val index: Int?,
    val description: String?,
    val title: String?,
    val active: Boolean?
)
