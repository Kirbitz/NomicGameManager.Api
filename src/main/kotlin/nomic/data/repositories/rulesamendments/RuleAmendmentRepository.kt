package nomic.data.repositories.rulesamendments

import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import nomic.domain.entities.AmendmentModel
import nomic.domain.entities.RulesAmendmentsModel
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.forEach
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
    override fun getRulesAmendments(gameId: Int): MutableList<RulesAmendmentsModel> {
        var currId: Int = -1
        val rules: MutableList<RulesAmendmentsModel> = mutableListOf()

        db.from(Rules)
            .leftJoin(Amendments, on = Amendments.ruleId eq Rules.ruleId)
            .select(Rules.ruleId, Rules.index, Rules.description, Rules.title, Rules.mutable, Amendments.amendId, Amendments.index, Amendments.description, Amendments.title, Amendments.active)
            .where(Rules.gameId eq gameId)
            .forEach { row ->
                if (currId != row[Rules.ruleId]) {
                    currId = row[Rules.ruleId]!!
                    rules += RulesAmendmentsModel(
                        row[Rules.ruleId]!!,
                        row[Rules.index]!!,
                        row[Rules.title]!!,
                        row[Rules.description],
                        row[Rules.mutable]!!
                    )
                }
                if (row[Amendments.amendId] != null) {
                    rules.last().amendments?.add(
                        AmendmentModel(
                            row[Amendments.amendId]!!,
                            row[Amendments.index]!!,
                            row[Amendments.description]!!,
                            row[Amendments.title]!!,
                            row[Amendments.active]!!
                        )
                    )
                }
            }

        return rules
    }
}
