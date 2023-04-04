package nomic.domain.rulesamendments

import nomic.api.models.RulesAmendmentsApiModel
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
     */
    fun repealRule(ruleId: String)

    /**
     * Collects the response from the database for changing the mutability of a rule
     *
     * @param mutableInput the boolean the rule will be changed to
     * @param ruleId the id of the rule to be repealed
     */
    fun transmuteRule(mutableInput: Boolean, ruleId: String)

    /**
     * Colects the response from the database for repealing and amendment
     *
     * @param amendId the id of the amendment to be repealed
     */
    fun repealAmendment(amendId: String)
}
