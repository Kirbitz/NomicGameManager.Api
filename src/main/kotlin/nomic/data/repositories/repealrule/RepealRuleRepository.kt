package nomic.data.repositories.repealrule

import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import org.springframework.stereotype.Service

@Service
class RepealRuleRepository(private val db: Database) : IRepealRuleRepository{
    override fun repealRule(ruleId: Int): Int {
        db.update(Amendments) {
            set(it.active, false)
            where {
                it.ruleId eq ruleId
            }
        }
        return db.update(Rules) {
            set(it.active, false)
            where {
                it.ruleId eq ruleId
            }
        }

    }
}