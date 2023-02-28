package nomic.domain.rulesamendments

import nomic.api.models.AmendmentModel
import nomic.api.models.RulesAmendmentsApiModel
import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
import nomic.domain.entities.RulesAmendmentsModel
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
    override fun getRulesAmendments(gameId: String): MutableList<RulesAmendmentsApiModel> {
        val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

        val rulesRaw: List<RulesAmendmentsModel> = ruleAmendmentRepository.getRulesAmendments(gameIdInt)
        val rules: MutableList<RulesAmendmentsApiModel> = mutableListOf()
        var currId: Int = -1

        rulesRaw.forEach { row ->
            if (currId != row.ruleId && row.ruleActive) {
                currId = row.ruleId
                rules += RulesAmendmentsApiModel(
                    row.ruleId,
                    row.ruleIndex,
                    row.ruleTitle,
                    row.ruleDescription,
                    row.ruleMutable
                )
            }
            if (row.amendId != null && row.ruleActive && row.amendActive!!) {
                rules.last().amendments?.add(
                    AmendmentModel(
                        row.amendId,
                        row.amendIndex!!,
                        row.amendDescription!!,
                        row.amendTitle!!
                    )
                )
            }
        }

        return rules
    }
}
