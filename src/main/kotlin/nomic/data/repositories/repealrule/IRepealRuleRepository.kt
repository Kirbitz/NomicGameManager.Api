package nomic.data.repositories.repealrule

/**
 * Interface returns status of the request
 *
 * @see [nomic.domain.entities.RepealRuleResponse]
 */
interface IRepealRuleRepository {
    /**
     * Changes active flag and returns success or fail
     *
     * @param ruleId The id of the rule to be repealed
     * @return Success or fail
     */
    fun repealRule(ruleId: Int): Int
}
