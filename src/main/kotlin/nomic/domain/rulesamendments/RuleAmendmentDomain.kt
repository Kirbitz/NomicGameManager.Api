package nomic.domain.rulesamendments

import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
import nomic.domain.entities.AmendmentModel
import nomic.domain.entities.RulesAmendmentsModel
import org.ktorm.dsl.Query
import org.ktorm.dsl.forEach
import org.springframework.stereotype.Service

/**
 * Implementation of the [IRuleAmendmentDomain][nomic.domain.rulesamendments.IRuleAmendmentDomain] uses
 * [RuleAmendmentRepository][nomic.data.repositories.rulesamendments.RuleAmendmentRepository] as a data layer
 *
 * @see [nomic.domain.rulesamendments.IRuleAmendmentDomain]
 * @see [nomic.data.repositories.rulesamendments.RuleAmendmentRepository]
 * @param ruleAmendmentRepository the instance of [RuleAmendmentRepository][nomic.data.repositories.rulesamendments.RuleAmendmentRepository]
 * to use as a data collector
 */
@Service
class RuleAmendmentDomain(
    private val ruleAmendmentRepository: RuleAmendmentRepository
) : IRuleAmendmentDomain {
    override fun getRulesAmendments(gameId: String): MutableList<RulesAmendmentsModel> {
        val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

        val result: Query = ruleAmendmentRepository.getRulesAmendments(gameIdInt)

        val rules: MutableList<RulesAmendmentsModel> = mutableListOf()

        var currId: Int? = -1
        result.forEach { row ->
            if (currId != row[Rules.ruleId]) {
                // If we get here, the row contains a new rule + amendment
                currId = row[Rules.ruleId]
                rules += RulesAmendmentsModel(
                    row[Rules.ruleId],
                    row[Rules.index],
                    row[Rules.title],
                    row[Rules.description],
                    row[Rules.mutable]
                )
            }
            if (row[Amendments.amendId] != null) {
                rules.last().amendments?.add(
                    AmendmentModel(
                        row[Amendments.amendId],
                        row[Amendments.index],
                        row[Amendments.description],
                        row[Amendments.title],
                        row[Amendments.active]
                    )
                )
            }
        }

        return rules
    }
}
