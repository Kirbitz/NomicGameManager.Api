package nomic.data.repositories.games

class GameRepository(private val db: Database) : IGameRepository{

        override fun createGame(title: String, currentPlayer: Int)
        {
                val createDate = LocalDate.now()
                
        }


}