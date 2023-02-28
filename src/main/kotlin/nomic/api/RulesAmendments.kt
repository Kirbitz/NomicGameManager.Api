package nomic.api

import nomic.data.EntityNotFoundException
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import nomic.domain.entities.RulesModel
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/rules_amendments")
class RulesAmendments(val ruleAmendmentDomain: RuleAmendmentDomain) {
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("{gameid}", produces = ["application/json;charset=UTF-8"])
    fun getRulesAmendments(@PathVariable(value = "gameid") gameId: String): ResponseEntity<Any> {
        val rulesAmendments: List<RulesAmendmentsModel> = ruleAmendmentDomain.getRulesAmendments(gameId)

        // Return the response object
        return ResponseEntity(rulesAmendments, HttpStatus.OK)
    }

    @PostMapping("enactRule")
    fun enactRule(@RequestBody inputRule:RulesModel): ResponseEntity<Any> {
        ruleAmendmentDomain.enactingRule(inputRule)
        return ResponseEntity("Rule Created", HttpStatus.CREATED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEntityNotFoundException(
        exception: EntityNotFoundException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, exception.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAllUncaughtException(
        exception: IllegalArgumentException
    ): ResponseEntity<Any> {
        return ResponseEntity(ExceptionResponseFormat(true, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
