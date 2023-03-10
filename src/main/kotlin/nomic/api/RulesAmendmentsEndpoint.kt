package nomic.api

import nomic.api.models.RulesAmendmentsApiModel
import nomic.domain.entities.RepealRuleResponse
import nomic.domain.entities.RulesModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * This controller listens on `api/rules_amendments` and works to provide, remove, or manipulate data related to games in the database
 *
 * @see[nomic.domain.rulesamendments.RuleAmendmentDomain]
 * @param[ruleAmendmentDomain] This dependency is a domain service used by the endpoint to collect Rules and Amendments data
 */
@RestController
@RequestMapping("api/rules_amendments")
class RulesAmendmentsEndpoint(val ruleAmendmentDomain: RuleAmendmentDomain) {

    /**
     * This endpoint listens on `api/rules_amendments/collect/ExistingGameId` and takes a gameId to collect the associated rules and amendments
     *
     * @param[gameId] The game id to collect rules and amendments on
     * @return A spring entity representing the response that gets serialized into JSON
     */
    @GetMapping("collect/{gameid}", produces = ["application/json;charset=UTF-8"])
    fun getRulesAmendments(@PathVariable(value = "gameid") gameId: String): ResponseEntity<Any> {
        val rulesAmendments: List<RulesAmendmentsApiModel> = ruleAmendmentDomain.getRulesAmendments(gameId)

        // Return the response object
        return ResponseEntity(rulesAmendments, HttpStatus.OK)
    }

    /**
     * This endpoint listens on `api/rules_amendments/enactRule` and takes a gameId to collect the associated rules and amendments
     *
     * @param[inputRule] Rule data to get inserted into the database
     * @return A spring entity representing the response that gets serialized into JSON
     */
    @PostMapping("enactRule")
    fun enactRule(@RequestBody inputRule: RulesModel): ResponseEntity<Any> {
        ruleAmendmentDomain.enactingRule(inputRule)
        return ResponseEntity("Rule Created", HttpStatus.CREATED)
    }

    /**
     * This endpoint listens on `api/rules_amendments/repeal_rule/ExistingRuleId` and takes a ruleId to change state to of active to false
     *
     * @param[ruleId] The rule id to change to inactive
     * @return A spring entity representing the response that gets serialized into JSON
     */
    @GetMapping("repeal_rule/{ruleid}", produces = ["application/json;charset=UTF-8"])
    fun repealRule(@PathVariable(value = "ruleid") ruleId: String): ResponseEntity<Any> {
        val repealRuleResponse: RepealRuleResponse = ruleAmendmentDomain.repealRule(ruleId)

        // Return the response object
        return ResponseEntity(repealRuleResponse, HttpStatus.OK)
    }
}
