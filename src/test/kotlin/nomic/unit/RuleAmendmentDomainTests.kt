package nomic.unit

import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.entities.RulesModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class RuleAmendmentDomainTests {
    private val ruleAmendmentDomain: RuleAmendmentDomain
    private val ruleAmendmentRepoMock: RuleAmendmentRepository

    init {
        val rawRuleList: MutableList<RulesAmendmentsModel> = mutableListOf(
            RulesAmendmentsModel(
                1234,
                12,
                "MyRule",
                "Rule and Amend True",
                false,
                true,
                12,
                404,
                "MyAmend",
                "Cool Description",
                true
            ),
            RulesAmendmentsModel(
                1234,
                12,
                "MyRule",
                "Rule True Amend False",
                false,
                true,
                13,
                405,
                "MyAmend2",
                "Cool Description",
                false
            ),
            RulesAmendmentsModel(
                666,
                12,
                "MyRule",
                "Rule True Amend False",
                false,
                true,
                12,
                404,
                "MyAmend",
                "Cool Description",
                false
            ),
            RulesAmendmentsModel(
                6789,
                12,
                "MyRule",
                "Rule False Amend True",
                false,
                false,
                12,
                404,
                "MyAmend",
                "Cool Description",
                true
            ),
            RulesAmendmentsModel(
                4567,
                12,
                "MyRule",
                "Rule and Amend false",
                false,
                false,
                12,
                404,
                "MyAmend",
                "Cool Description",
                false
            )
        )

        ruleAmendmentRepoMock = mock {
            on { getRulesAmendments(anyInt()) } doReturn rawRuleList
        }

        ruleAmendmentDomain = RuleAmendmentDomain(ruleAmendmentRepoMock)
    }

    @Test
    fun `Get Rules And Amendments Valid Game Id`() {
        val result = ruleAmendmentDomain.getRulesAmendments("1234")

        Assertions.assertThat(result.size).isEqualTo(2)
        Assertions.assertThat(result[0].amendments.size).isEqualTo(1)
        Assertions.assertThat(result[1].amendments.size).isEqualTo(0)
    }

    @Test
    fun `Get Rules And Amendments Bad Game Id`() {
        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.getRulesAmendments("apple")
        }
    }

    @Test
    fun `Repeal Rule Valid Rule Id`() {
        val result = ruleAmendmentDomain.repealRule("1234")

        Assertions.assertThat(result.message).isEqualTo("Updated Successfully")
    }

    @Test
    fun `Repeal Rule Bad Rule Id`() {
        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.repealRule("penpineappleapplepen")
        }
    }

    @Test
    fun `Enact Rule Valid Rule Data`() {
        val inputRule = RulesModel(123, 23, "MyRule", "Description?", false, 2)

        ruleAmendmentDomain.enactingRule(inputRule)

        verify(ruleAmendmentRepoMock, times(1)).enactRule(inputRule)
    }

    @Test
    fun `Enact Rule Bad Rule Data - Title`() {
        val inputRule = RulesModel(123, 23, "&!@#$", "Description?", false, 2)

        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.enactingRule(inputRule)
        }
    }

    @Test
    fun `Enact Rule Bad Rule Data - Description`() {
        val inputRule = RulesModel(123, 23, "MyRule", "!@#$", false, 2)

        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.enactingRule(inputRule)
        }
    }
}