package nomic.api.models

/**
 * The games object to be passed back to the caller of the endpoint
 *
 * @property title the name of this game
 * @property userId the id of the user the game is linked to
 */
data class GamesApiModel(
    val title: String,
    val userId: Int
)
