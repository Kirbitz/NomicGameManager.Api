package nomic.api

import nomic.domain.entities.RulesAmendmentsModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/repeal_rules")
class RepealingRules {
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("{ruleid}", produces = ["application/json;charset=UTF-8"])
    fun getRulesAmendments(@PathVariable(value = "ruleid") ruleId: String): ResponseEntity<Any> {

        // Return the response object
        return ResponseEntity("", HttpStatus.OK)
    }
    // Connect to the database

    // Get the ID of the rule that is to be repealed

    // Verify that the ID is valid, and check if it is in the database. Else return error

    // Send an update query to change the visible flag on the rule to be repealed, return success
}