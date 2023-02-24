package nomic.data.repositories.rulesamendments

import nomic.data.EntityNotFoundException
import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.Query
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.springframework.stereotype.Repository

/**
 * Implementation of the [IRuleAmendmentRepository][nomic.data.repositories.rulesamendments.IRuleAmendmentRepository] uses
 * Ktorm [Database][org.ktorm.database.Database] as the data access layer
 *
 * @see [nomic.data.repositories.rulesamendments.IRuleAmendmentRepository]
 * @see [org.ktorm.database.Database]
 * @param db the connected instance of [org.ktorm.database.Database] to use as the database
 */
@Repository
class RuleAmendmentRepository(private val db: Database) : IRuleAmendmentRepository {
    override fun getRulesAmendments(gameId: Int): Query {
        val result = db.from(Rules)
            .leftJoin(Amendments, on = Amendments.ruleId eq Rules.ruleId)
            .select()
            .where(Rules.gameId eq gameId)

        if (result.totalRecordsInAllPages == 0) {
            throw EntityNotFoundException(gameId)
        }

        return result
    }
}
