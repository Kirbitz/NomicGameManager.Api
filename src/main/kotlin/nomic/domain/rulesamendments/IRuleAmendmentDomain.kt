package nomic.domain.rulesamendments

import nomic.domain.entities.RulesAmendmentsModel

interface IRuleAmendmentDomain {
    fun getRulesAmendments(gameId: String) : MutableList<RulesAmendmentsModel>
}