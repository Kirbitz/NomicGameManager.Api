package game.manager.nomic.api

import com.fasterxml.jackson.databind.ObjectMapper
import game.manager.nomic.api.config.DatabaseConfig
import game.manager.nomic.api.config.NomicConfigProperties
import models.Rule
import models.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.jackson.KtormModule
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class Rules_Amendments {

    private val database: Database
    private val objectMapper: ObjectMapper

    constructor(nomicConfig: NomicConfigProperties) {
        database = DatabaseConfig(nomicConfig).connectDB()
        objectMapper = ObjectMapper()
        objectMapper.registerModule(KtormModule())
    }
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("rules_amendments/{gameid}")
    fun getRulesAmendments(@PathVariable(value="gameid") gameId : String): ResponseEntity<Any> {
        var responseHeader : HttpHeaders = HttpHeaders()
        responseHeader.set("Content-Type", "application/json")
        responseHeader.set("charset", "UTF-8")

        var rule: List<Rule> = listOf<Rule>()

        try {
            if(gameId.isEmpty() || !gameId.matches(Regex("\\d+"))) {
                throw IllegalArgumentException("Please enter a valid GameId!")
            }

            rule = database.from(Rules)
                .select(Rules.ruleId, Rules.index, Rules.title, Rules.description, Rules.mutable)
                .map { row -> Rules.createEntity(row) }
        } catch(exception: IllegalArgumentException) {
            return ResponseEntity.badRequest().headers(responseHeader).body(exception.message)
        } catch(exception: Exception) {
            return ResponseEntity.internalServerError().headers(responseHeader).body("Internal Server Error!")
        }

        return ResponseEntity.ok().headers(responseHeader).body(objectMapper.writeValueAsString(rule))
    }
}