package nomic.api

import nomic.api.models.RulesAmendmentsApiModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/rules_amendments")
class RulesAmendments(val ruleAmendmentDomain: RuleAmendmentDomain) {
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("{gameid}", produces = ["application/json;charset=UTF-8"])
    fun getRulesAmendments(@PathVariable(value = "gameid") gameId: String): ResponseEntity<Any> {
        val rulesAmendments: List<RulesAmendmentsApiModel> = ruleAmendmentDomain.getRulesAmendments(gameId)

        // Return the response object
        return ResponseEntity(rulesAmendments, HttpStatus.OK)
    }
}
