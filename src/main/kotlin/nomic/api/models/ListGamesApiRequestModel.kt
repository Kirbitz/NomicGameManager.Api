package nomic.api.models

data class ListGamesApiRequestModel(
    val size: UInt = 100U,
    val offset: UInt = 0U
)
