package nomic.domain.rulesamendments

import nomic.api.models.RulesAmendmentsApiModel
import nomic.domain.entities.RepealRuleResponse

/**
 * This manages the call and format of the rules and amendments data
 */
interface IRuleAmendmentDomain {
    /**
     * Collects and formats the rules and amendments data
     *
     * @param gameId The id of the game to collect rule and amendment data on
     * @return The list of [RulesAmendmentsModel][nomic.domain.entities.RulesAmendmentsModel] objects to pass back to the API layer
     */
    fun getRulesAmendments(gameId: String): MutableList<RulesAmendmentsApiModel>

    /**
     * Collects the response from the database for repealing a rule
     *
     * @param ruleId the id of the rule to be repealed
     * @return A [RepealRuleResponse][nomic.domain.entities.RepealRuleResponse] object to pass back the status
     */
    fun repealRule(ruleId: String): RepealRuleResponse
}
