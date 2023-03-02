package nomic.api.models

/**
 * The rules and amendments object to be passed back to the caller of the endpoint
 *
 * @property title the name of this game
 * @property userId the id of the user the game is linked to
 * @see[nomic.api.models.GameModel]
 */
data class GamesApiModel(
    val title: String,
    val userId: Int,
)