package nomic.domain.repealrule

import nomic.domain.entities.RepealRuleResponse

/**
 * This manages the call and response of the repeal rule endpoint
 */
interface IRepealRuleDomain {
    /**
     * Collects the response from the database for repealing a rule
     *
     * @param ruleId the id of the rule to be repealed
     * @return A RepealRuleResponse [nomic.domain.entities.RepealRuleResponse] object to pass back the status
     */
    fun repealRule(ruleId: String): RepealRuleResponse
}
