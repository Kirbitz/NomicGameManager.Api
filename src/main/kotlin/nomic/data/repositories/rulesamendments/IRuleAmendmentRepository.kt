package nomic.data.repositories.rulesamendments

import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.RulesModel

/**
 * Interface returns the obtained and formatted RulesAmendments data from the DB
 *
 * @see [nomic.domain.entities.RulesAmendmentsModel]
 */
interface IRuleAmendmentRepository {
    /**
     * Collects the raw rule and amendment information for a specific game
     *
     * @param gameId The id of the game to collect rule and amendment data on
     * @return MutableList<RulesAmendmentsModel> rules and amendments data for a specific game
     */
    fun getRulesAmendments(gameId: Int): MutableList<RulesAmendmentsModel>

    /**
     * Creates a rule
     * @param inputRule Model of the [RulesModel][nomic.domain.entities.RulesModel] rule to be made
     */
    fun enactRule(inputRule: RulesModel)

    /**
     * Changes active flag and returns success or fail
     *
     * @param ruleId The id of the rule to be repealed
     */
    fun repealRule(ruleId: Int)
}
