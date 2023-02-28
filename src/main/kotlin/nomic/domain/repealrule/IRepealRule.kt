package nomic.domain.repealrule

import nomic.domain.entities.RepealRuleResponse

interface IRepealRule {
    fun repealRule(ruleId: String): RepealRuleResponse
}