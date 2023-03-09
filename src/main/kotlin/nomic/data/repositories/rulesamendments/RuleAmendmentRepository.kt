package nomic.data.repositories.rulesamendments

import nomic.data.EntityNotFoundException
import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.RulesModel
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.dsl.forEach
import org.ktorm.dsl.update
import org.ktorm.dsl.insert
import org.ktorm.dsl.eq
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
        val rules: MutableList<RulesAmendmentsModel> = mutableListOf()

        db.from(Rules)
            .leftJoin(Amendments, on = Amendments.ruleId eq Rules.ruleId)
            .select(Rules.ruleId, Rules.index, Rules.description, Rules.title, Rules.mutable, Rules.active, Amendments.amendId, Amendments.index, Amendments.description, Amendments.title, Amendments.active)
            .where(Rules.gameId eq gameId)
            .forEach { row ->
                rules += RulesAmendmentsModel(
                    row[Rules.ruleId]!!,
                    row[Rules.index]!!,
                    row[Rules.title]!!,
                    row[Rules.description]!!,
                    row[Rules.mutable]!!,
                    row[Rules.active]!!,
                    row[Amendments.amendId],
                    row[Amendments.index],
                    row[Amendments.title],
                    row[Amendments.description],
                    row[Amendments.active]
                )
            }

        return rules
    }

    override fun repealRule(ruleId: Int) {
        db.update(Amendments) {
            set(it.active, false)
            where {
                it.ruleId eq ruleId
            }
        }
        val result = db.update(Rules) {
            set(it.active, false)
            where {
                it.ruleId eq ruleId
            }
        }

        if (result < 1) {
            throw EntityNotFoundException(ruleId)
        }
    }

    override fun enactRule(inputRule: RulesModel) {
        //Check to see if game exists
        db.insert(Rules) {
            set(it.gameId, inputRule.gameID)
            set(it.mutable, inputRule.mutable)
            set(it.index, inputRule.index)
            set(it.title, inputRule.title)
            set(it.description, inputRule.description)
        }
    }
}
