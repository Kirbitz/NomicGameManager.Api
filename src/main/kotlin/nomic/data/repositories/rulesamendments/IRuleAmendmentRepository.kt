package nomic.data.repositories.rulesamendments

import org.ktorm.dsl.Query

interface IRuleAmendmentRepository {
    fun getRulesAmendments(gameId: Int): Query
}