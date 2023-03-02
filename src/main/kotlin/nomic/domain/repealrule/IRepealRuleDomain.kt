package nomic.domain.repealrule

import nomic.domain.entities.RepealRuleResponse

interface IRepealRuleDomain {
    fun repealRule(ruleId: String): RepealRuleResponse
}