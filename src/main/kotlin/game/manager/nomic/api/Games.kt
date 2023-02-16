package game.manager.nomic.api

import game.manager.nomic.api.config.DatabaseConfig
import game.manager.nomic.api.config.NomicConfigProperties
import models.Game
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.anootation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Rest Controller
@RequestMapping("api")
class Game{

    private val database: Database

    constructor(nomicConfig: NomicConfigProperties){
        database = DatabaseConfig(nomicConfig).connectDB()
    }

    // Path to endpoint is api/games/ExistingUserId
    @GetMapping("games/{userId}")
    fun getGames(@PathVariable(value="userId") userId : String): ResponseEntity<Any> {
        database.from(Games).select(Games.title).forEach { row -> println(row[Games.title]) }
        return ResponseEntity<Any>(Game(21, "Great", "Hello World"), HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/games")
    fun addNewGame(@RequestBody game: Game): Game {
        database.save(Game)
    }
}