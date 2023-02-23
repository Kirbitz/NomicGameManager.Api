package nomic.api

import nomic.data.EntityNotFoundException
import nomic.domain.entities.RulesAmendmentsModel
import nomic.domain.rulesamendments.RuleAmendmentDomain
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class RulesAmendments(val ruleAmendmentDomain: RuleAmendmentDomain) {
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("rules_amendments/{gameid}")
    fun getRulesAmendments(@PathVariable(value="gameid") gameId : String): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        responseHeader.set("Content-Type", "application/json")
        responseHeader.set("charset", "UTF-8")

        val rulesAmendments: MutableList<RulesAmendmentsModel>

        try {
            rulesAmendments = ruleAmendmentDomain.getRulesAmendments(gameId)
        } catch(exception: IllegalArgumentException) {
            val illegalArgumentProblem = NomicProblemDetails(true, "gameId was not a valid integer")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(responseHeader).body(illegalArgumentProblem)
        } catch(exception: EntityNotFoundException) {
            val notFoundException = NomicProblemDetails(true, "No rules were found for that game")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(responseHeader).body(notFoundException)
        } catch(exception: Exception) {
            val internalError = NomicProblemDetails(true, "Internal server error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(responseHeader).body(internalError)
        }

        // Return the response object
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeader).body(rulesAmendments)
    }
}