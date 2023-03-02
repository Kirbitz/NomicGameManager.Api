package nomic.api

import nomic.domain.entities.RepealRuleResponse
import nomic.domain.repealrule.RepealRuleDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/repeal_rule")
class RepealRule (val repealRuleDomain: RepealRuleDomain){
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("{ruleid}", produces = ["application/json;charset=UTF-8"])
    fun repealRule(@PathVariable(value = "ruleid") ruleId: String): ResponseEntity<Any> {
        val repealRuleResponse: RepealRuleResponse = repealRuleDomain.repealRule(ruleId)

        // Return the response object
        return ResponseEntity(repealRuleResponse, HttpStatus.OK)
    }
}