package nomic.domain.rulesamendments

import nomic.api.models.RulesAmendmentsApiModel
import nomic.domain.entities.RepealRuleResponse
import nomic.domain.entities.RulesModel

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
     * Collects and formats the rules data
     *
     * @param input Model of the [RulesModel][nomic.domain.entities.RulesModel] to be made
     */
    fun enactingRule(input: RulesModel)

    /**
     * Collects the response from the database for repealing a rule
     *
     * @param ruleId the id of the rule to be repealed
     * @return A [RepealRuleResponse][nomic.domain.entities.RepealRuleResponse] object to pass back the status
     */
    fun repealRule(ruleId: String): RepealRuleResponse

    /**
     * Changes a rule in the database and changes its mutability to the input boolean
     * @param ruleId the id of the rule being changes
     * @param mutableInput is what the rule's mutability will be set to
     * @return A [TransmuteRuleResponse][nomic.domain.entities.RepealRuleResponse] object to pass back the status
     */
    fun transmuteRule(mutableInput: Boolean, ruleId: String): TransmuteRuleResponse
}
