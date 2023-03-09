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
}
