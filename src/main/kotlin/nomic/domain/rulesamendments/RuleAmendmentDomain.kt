package nomic.domain.rulesamendments

import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
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
    override fun getRulesAmendments(gameId: String): MutableList<RulesAmendmentsModel> {
        val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

        return ruleAmendmentRepository.getRulesAmendments(gameIdInt)
    }
    override fun enactingRule(input: RulesModel) {
        val regex = "^[A-Za-z0-9 .!?]$".toRegex()

        if(!regex.matches(input.description!!)) {
            throw IllegalArgumentException("Has Special Characters")
        }
        if(!regex.matches(input.title)) {
            throw IllegalArgumentException("Has Special Characters")
        }

        ruleAmendmentRepository.enactRule(input)
    }
}
