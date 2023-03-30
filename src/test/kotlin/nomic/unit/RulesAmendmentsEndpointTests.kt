package nomic.unit

import nomic.api.RulesAmendmentsEndpoint
import nomic.api.models.AmendmentModel
import nomic.api.models.ResponseFormat
import nomic.api.models.RulesAmendmentsApiModel
import nomic.domain.entities.RulesModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus

class RulesAmendmentsEndpointTests {
    private val rulesAmendmentsEndpoint: RulesAmendmentsEndpoint

    init {
        val ruleAmendmentList = mutableListOf(
            RulesAmendmentsApiModel(
                1234,
                200,
                "My Awesome Rule",
                "Wow this rule is cool",
                false,
                mutableListOf(
                    AmendmentModel(
                        420,
                        100101,
                        "Nevermind this rule sucks",
                        "Amend the Crappy Rule"
                    )
                )
            )
        )

        val ruleAmendmentDomainMock: RuleAmendmentDomain = mock {
            on { getRulesAmendments(anyString()) } doReturn ruleAmendmentList
        }

        rulesAmendmentsEndpoint = RulesAmendmentsEndpoint(ruleAmendmentDomainMock)
    }

    @Test
    fun `Get Rule and Amendments with Game Id Provided and Proper Response Object Returned`() {
        val result = rulesAmendmentsEndpoint.getRulesAmendments("2134").body as ResponseFormat<List<RulesAmendmentsApiModel>>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.data.size).isEqualTo(1)
        Assertions.assertThat(result.data[0].amendments.size).isEqualTo(1)
    }

    @Test
    fun `Enact Rule with Rule Object Provided and Proper Response Object Returned`() {
        val ruleInput = RulesModel(1234, 666, "Wow", "Cool Description", false, 202)
        val result = rulesAmendmentsEndpoint.enactRule(ruleInput).body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(result.data).contains("Rule Created")
    }

    @Test
    fun `Repeal Rule with Rule Id Provided and Proper Response Object Returned`() {
        val result = rulesAmendmentsEndpoint.repealRule("4321").body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.data).contains("Rule Repealed")
    }

    @Test
    fun `Transmute Rule with Rule Id & Boolean Provided and Proper Response Object Returned`() {
        val result = rulesAmendmentsEndpoint.transmuteRule(true, "4321").body as ResponseFormat<String>

        Assertions.assertThat(result.success).isTrue
        Assertions.assertThat(result.status).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(result.data).contains("Rule Transmuted")
    }
}
