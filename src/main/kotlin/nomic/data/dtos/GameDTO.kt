package nomic.data.dtos

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.ktorm.schema.date
import java.time.LocalDate

/**
 * Data Class for a Game
 *
 * @property gameId the unique id for this game
 * @property title the name of this game
 * @property createDate the date when this game was created
 * @property currentPlayer the id of player who's turn it is
 * @property userId the id of the user that made this game
 */

interface Game: Entity<Game> {
    companion object : Entity.Factory<Game>()
    val gameId: Int
    val title: String
    val createDate: LocalDate
    val currentPlayer: Int
    val userId: Int
}

/**
 * This object represents the games table in the database, employing KTorm's framework.
 *
 * @see[org.ktorm.schema.Table]
 * @property gameId The primary key for the [Games] table
 * @property title the name of this game
 * @property createDate the date when this game was created
 * @property currentPlayer the id of player who's turn it is
 * @property userId The foreign key to the Users table
 */
object Games : Table<Game>("Game") {
    val gameId = int("gameId").primaryKey().bindTo {it.gameId}
    val title = varchar("title").bindTo {it.title}
    val createDate = date("createDate").bindTo {it.createDate}
    val currentPlayer = int("currentPlayer").bindTo {it.currentPlayer}
    val userId = int("userId").bindTo {it.userId}
}