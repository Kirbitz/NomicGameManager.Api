package nomic.data.repositories.rulesamendments

import nomic.domain.entities.RulesAmendmentsModel

/**
 * Interface returns the obtained query when prompting the DB for Rules and Amendments
 *
 * @see [nomic.data.dtos.RulesAmendmentsDTO]
 */
interface IRuleAmendmentRepository {
    /**
     * Collects the raw rule and amendment information for a specific game
     *
     * @param gameId The id of the game to collect rule and amendment data on
     * @return Raw rule and amendment data for a specific game
     */
    fun getRulesAmendments(gameId: Int): MutableList<RulesAmendmentsModel>
}
