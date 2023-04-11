package nomic.data.dtos

import nomic.data.dtos.Games.createDate
import nomic.data.dtos.Games.currentPlayer
import nomic.data.dtos.Games.gameId
import nomic.data.dtos.Games.title
import nomic.data.dtos.Games.userId
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDate

/**
 * Interface for Game Table
 *
 * @property gameId the unique id for this game
 * @property title the name of this game
 * @property createDate the date when this game was created
 * @property currentPlayer the id of player whose turn it is
 * @property userId the id of the user that made this game
 */

interface GameDTO : Entity<GameDTO> {
    companion object : Entity.Factory<GameDTO>()
    val gameId: Int
    val title: String
    val createDate: LocalDate
    val currentPlayer: Int
    val userId: Int
}

/**
 * This object represents the credentials table in the database, employing KTorm's framework.
 *
 * @see[org.ktorm.schema.Table]
 * @property gameId The primary key for the [Games] table
 * @property title What the game is called
 * @property createDate the date when this game was created
 * @property currentPlayer the id of player whose turn it is
 * @property userId the id of the user that made this game
 */
object Games : Table<GameDTO>("Game") {
    val gameId = int("gameId").primaryKey().bindTo { it.gameId }
    val title = varchar("title").bindTo { it.title }
    val createDate = date("createDate").bindTo { it.createDate }
    val currentPlayer = int("currentPlayer").bindTo { it.currentPlayer }
    val userId = int("userId").bindTo { it.userId }
}

val Database.games get() = this.sequenceOf(Games)
