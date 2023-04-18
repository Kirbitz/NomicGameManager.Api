package nomic.api.models

/**
 * This model encapsulates the parameters for listing games from the API endpoint
 *
 * @param[size] The size of the batch of games to retrieve. Maximum size is 100
 * games, and the default is 100.
 * @param[offset] How much the batch should be offset. This is used for paginating
 * games, and the default to 0.
 */
data class ListGamesApiRequestModel(
    val size: UInt = 100U,
    val offset: UInt = 0U
)
