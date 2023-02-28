package nomic.data.repositories.repealrule

interface IRepealRuleRepository {
    fun repealRule(ruleId: Int): Int
}