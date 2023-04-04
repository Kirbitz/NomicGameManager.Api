package nomic.domain.rulesamendments

import nomic.api.models.AmendmentModel
import nomic.api.models.RulesAmendmentsApiModel
import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
import nomic.domain.entities.AmendmentInputModel
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.RulesModel
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
                rules.last().amendments.add(
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

    override fun repealRule(ruleId: String) {
        val ruleIdInt: Int = ruleId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid ruleId!")

        ruleAmendmentRepository.repealRule(ruleIdInt)
    }

    override fun repealAmendment(amendId: String) {
        val amendIdInt: Int = amendId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid amendId")

        ruleAmendmentRepository.repealAmendment(amendIdInt)
    }

    override fun enactingRule(input: RulesModel) {
        val regex = "^[A-Za-z0-9 ,.!?]*$".toRegex()

        if (!regex.matches(input.description!!)) {
            throw IllegalArgumentException("Has Special Characters")
        }
        if (!regex.matches(input.title)) {
            throw IllegalArgumentException("Has Special Characters")
        }

        ruleAmendmentRepository.enactRule(input)
    }

    override fun enactAmendment(amend: AmendmentInputModel) {
        val regex = "^[A-Za-z0-9 ,.!?]*$".toRegex()

        if (!regex.matches(amend.description!!)) {
            throw IllegalArgumentException("Has Special Characters")
        }
        if (!regex.matches(amend.title)) {
            throw IllegalArgumentException("Has Special Characters")
        }

        ruleAmendmentRepository.enactAmendment(amend)
    }

    override fun transmuteRule(mutableInput: Boolean, ruleId: String) {
        val ruleIdInt: Int = ruleId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid ruleId!")

        ruleAmendmentRepository.transmuteRule(mutableInput, ruleIdInt)
    }
}
