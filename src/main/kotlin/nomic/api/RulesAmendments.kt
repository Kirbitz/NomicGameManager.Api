package nomic.api

import com.fasterxml.jackson.databind.ObjectMapper
import nomic.data.DatabaseProvider
import nomic.data.DatabaseConfigProperties
import models.AmendmentModel
import models.RulesAmendmentsModel
import nomic.data.dtos.Amendments
import nomic.data.dtos.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class RulesAmendments(val nomicConfig: DatabaseConfigProperties) {

    private val databaseConfig: DatabaseProvider = DatabaseProvider()

    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("rules_amendments/{gameid}")
    fun getRulesAmendments(@PathVariable(value="gameid") gameId : String): ResponseEntity<Any> {
        val responseHeader = HttpHeaders()
        responseHeader.set("Content-Type", "application/json")
        responseHeader.set("charset", "UTF-8")

        val rules: MutableList<RulesAmendmentsModel> = mutableListOf()

        try {
            val database: Database = databaseConfig.getDatabase(nomicConfig)

            val gameIdInt: Int = gameId.toIntOrNull() ?: throw IllegalArgumentException("Please enter a valid GameId!")

            var currId: Int? = -1
            database.from(Rules)
                .leftJoin(Amendments, on = Amendments.ruleId eq Rules.ruleId)
                .select()
                .where (Rules.gameId eq gameIdInt )
                .forEach { row ->
                    if (currId != row[Rules.ruleId]) {
                        // If we get here, the row contains a new rule + amendment
                        currId = row[Rules.ruleId]
                        rules += RulesAmendmentsModel(
                            row[Rules.ruleId],
                            row[Rules.index],
                            row[Rules.title],
                            row[Rules.description],
                            row[Rules.mutable]
                        )
                    }
                    if(row[Amendments.amendId] != null) {
                        rules.last().amendments?.add(
                            AmendmentModel(
                                row[Amendments.amendId],
                                row[Amendments.index],
                                row[Amendments.description],
                                row[Amendments.title],
                                row[Amendments.active]
                            )
                        )
                    }
                }

        } catch(exception: IllegalArgumentException) {
            val illegalArgumentProblem = NomicProblemDetails(true, "gameId was not a valid integer")
            return ResponseEntity.badRequest().headers(responseHeader).body(illegalArgumentProblem)
        } catch(exception: Exception) {
            val internalError = NomicProblemDetails(true, "Internal server error")
            return ResponseEntity.internalServerError().headers(responseHeader).body(internalError)
        }

        // Return the response object
        return ResponseEntity.ok().headers(responseHeader).body(rules)
    }
}