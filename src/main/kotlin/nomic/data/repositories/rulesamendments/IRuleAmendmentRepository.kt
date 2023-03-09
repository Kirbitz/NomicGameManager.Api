package nomic.data.repositories.rulesamendments

import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.RulesModel

/**
 * Interface returns the obtained query when prompting the DB for Rules and Amendments
 *
 * @see [nomic.domain.entities.RulesAmendmentsModel]
 */
interface IRuleAmendmentRepository {
    /**
     * Collects the raw rule and amendment information for a specific game
     *
     * @param gameId The id of the game to collect rule and amendment data on
     * @return Raw rule and amendment data for a specific game
     */
    fun getRulesAmendments(gameId: Int): MutableList<RulesAmendmentsModel>
    /**
     * Creates a rule
     * @param inputRule Model of the [RulesModel][nomic.domain.entities.RulesModel] rule to be made
     */
    fun enactRule(inputRule: RulesModel)
}
