package models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.ktorm.schema.date

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
    val createDate: String
    val currentPlayer: Int
    val userId: Int
}

object Games : Table<Game>("Game") {
    val gameId = int("gameId").primaryKey().bindTo {it.gameId}
    val title = varchar("title").bindTo {it.title}
    val createDate: date("createDate").bindTo {it.createDate}
    val currentPlayer: int("currentPlayer").bindTo {it.currentPlayer}
    val userId: int("userId").bindTo {it.userID}
}



