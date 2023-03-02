package nomic.domain.entities

import java.time.LocalDate

/**
 * Data Class for a Game
 *
 * @property gameId the unique id for this game
 * @property title the name of this game
 * @property createDate the date when this game was created
 * @property currentPlayer the id of player whose turn it is
 * @property userId the id of the user that made this game
 */

data class GameModel(
    val gameId: Int? = null,
    val title: String,
    val createDate: LocalDate? = null,
    val currentPlayer: Int? = null,
    val userId: Int,
)