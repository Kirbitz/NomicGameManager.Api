package game.manager.nomic.api

import game.manager.nomic.api.config.DatabaseConfig
import game.manager.nomic.api.config.NomicConfigProperties
import models.Amendment
import models.Rules
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class Rules_Amendments {

    private val database : Database

    constructor(nomicConfig: NomicConfigProperties) {
        database = DatabaseConfig(nomicConfig).connectDB()
    }
    // Path to endpoint is api/rules_amendments/ExistingGameId
    @GetMapping("rules_amendments/{gameid}")
    fun getRulesAmendments(@PathVariable(value="gameid") gameId : String): ResponseEntity<Any> {
        database.from(Rules).select(Rules.title).forEach { row -> println(row[Rules.title]) }
        return ResponseEntity<Any>(Amendment(21, 1, "Hello World"), HttpStatus.BAD_REQUEST)
    }
}