package nomic.data.repositories.rulesamendments

import org.ktorm.dsl.Query


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
    fun getRulesAmendments(gameId: Int): Query
}