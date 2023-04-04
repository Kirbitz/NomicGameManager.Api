package nomic.unit

import nomic.data.repositories.rulesamendments.RuleAmendmentRepository
import nomic.domain.entities.AmendmentInputModel
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
                ruleId = 1234,
                ruleIndex = 12,
                ruleTitle = "MyRule",
                ruleDescription = "Rule and Amend True",
                ruleMutable = false,
                ruleActive = true,
                amendId = 12,
                amendIndex = 404,
                amendTitle = "MyAmend",
                amendDescription = "Cool Description",
                amendActive = true
            ),
            RulesAmendmentsModel(
                ruleId = 1234,
                ruleIndex = 12,
                ruleTitle = "MyRule",
                ruleDescription = "Rule True Amend False",
                ruleMutable = false,
                ruleActive = true,
                amendId = 13,
                amendIndex = 405,
                amendTitle = "MyAmend2",
                amendDescription = "Cool Description",
                amendActive = false
            ),
            RulesAmendmentsModel(
                ruleId = 666,
                ruleIndex = 12,
                ruleTitle = "MyRule",
                ruleDescription = "Rule True Amend False",
                ruleMutable = false,
                ruleActive = true,
                amendId = 12,
                amendIndex = 404,
                amendTitle = "MyAmend",
                amendDescription = "Cool Description",
                amendActive = false
            ),
            RulesAmendmentsModel(
                ruleId = 6789,
                ruleIndex = 12,
                ruleTitle = "MyRule",
                ruleDescription = "Rule False Amend True",
                ruleMutable = false,
                ruleActive = false,
                amendId = 12,
                amendIndex = 404,
                amendTitle = "MyAmend",
                amendDescription = "Cool Description",
                amendActive = true
            ),
            RulesAmendmentsModel(
                ruleId = 4567,
                ruleIndex = 12,
                ruleTitle = "MyRule",
                ruleDescription = "Rule and Amend False",
                ruleMutable = false,
                ruleActive = false,
                amendId = 12,
                amendIndex = 404,
                amendTitle = "MyAmend",
                amendDescription = "Cool Description",
                amendActive = false
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
        ruleAmendmentDomain.repealRule("1234")

        verify(ruleAmendmentRepoMock, times(1)).repealRule(1234)
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

    @Test
    fun enactAmendment_validData() {
        val amendment = AmendmentInputModel(1234, 1, 101, "New Amendment", "Lorem ipsum dolor sit amet consectetur adipiscing elit. Praesent eleifend.")

        ruleAmendmentDomain.enactAmendment(amendment)

        verify(ruleAmendmentRepoMock, times(1)).enactAmendment(amendment)
    }

    @Test
    fun enactAmendment_invalidTitle_fails() {
        val amendment = AmendmentInputModel(1234, 1, 101, "---New Amendment---", "Lorem ipsum dolor sit amet consectetur adipiscing elit. Praesent eleifend.")

        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.enactAmendment(amendment)
        }
    }

    @Test
    fun enactAmendment_invalidDescription_fails() {
        val amendment = AmendmentInputModel(1234, 1, 101, "New Amendment", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent eleifend.")

        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.enactAmendment(amendment)
        }
    }

    @Test
    fun `Repeal Amendment Valid Amend Id`() {
        ruleAmendmentDomain.repealAmendment("1234")

        verify(ruleAmendmentRepoMock, times(1)).repealAmendment(1234)
    }

    @Test
    fun `Repeal Amendment Bad Amend Id`() {
        assertThrows(IllegalArgumentException::class.java) {
            ruleAmendmentDomain.repealAmendment("AdamIsBad")
        }
    }
}
