package nomic.domain.repealrule

import nomic.data.repositories.repealrule.RepealRuleRepository
import nomic.domain.entities.RepealRuleResponse
import org.springframework.stereotype.Service

@Service
class RepealRuleDomain (private val repealRuleRepository: RepealRuleRepository) : IRepealRuleDomain {

    override fun repealRule(ruleId: String): RepealRuleResponse {
        val ruleIdInt: Int = ruleId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")
        val result: Int = repealRuleRepository.repealRule(ruleIdInt)
        if(result == 0){
            return RepealRuleResponse(false, "No Rows returned", ruleIdInt)
        }
        return RepealRuleResponse(true, "Updated Successfully", ruleIdInt)
    }
}