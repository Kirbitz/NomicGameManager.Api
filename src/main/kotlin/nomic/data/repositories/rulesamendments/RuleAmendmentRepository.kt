package nomic.data.repositories.rulesamendments

import nomic.data.dtos.amendments
import nomic.data.dtos.rules
import nomic.domain.entities.AmendmentModel
import nomic.domain.entities.RulesAmendmentsModel
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.filter
import org.ktorm.entity.toCollection
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
        val rulesDict = mutableMapOf<Int, RulesAmendmentsModel>()

        val rules = db.rules.filter {
            (it.gameId eq gameId) and (it.active eq true)
        }.toCollection(mutableListOf())

        val amendments = db.amendments.filter {
            (it.rule.gameId eq gameId) and (it.rule.active eq true) and (it.active eq true)
        }.toCollection(mutableListOf())

        for (rule in rules) {
            rulesDict.put(
                rule.ruleId,
                RulesAmendmentsModel(
                    rule.ruleId,
                    rule.index,
                    rule.title,
                    rule.description,
                    rule.mutable
                )
            )
        }

        for (amendment in amendments) {
            rulesDict[amendment.rule.ruleId]!!.amendments.add(
                AmendmentModel(
                    amendment.amendId,
                    amendment.index,
                    amendment.description,
                    amendment.title
                )
            )
        }

        return rulesDict.values.toMutableList()
    }
}
