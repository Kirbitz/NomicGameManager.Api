package nomic.data.repositories.repealrule

import nomic.data.dtos.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.update


class RepealRuleRepository(private val db: Database) : IRepealRuleRepository{
    override fun repealRule(ruleId: Int): Int{
        db.update(Rules){
            set(Rules.active, false)
        }

    }
}