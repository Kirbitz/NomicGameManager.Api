package nomic.data.repositories.games

import nomic.data.dtos.Games
import nomic.domain.entities.GameModel
import org.ktorm.database.Database
import org.ktorm.dsl.insert
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class GameRepository(private val db: Database) : IGameRepository{

        override fun createGame(input: GameModel)
        {
                //Check to see if game exists
                db.insert(Games){
                        set(it.title, input.title)
                        set(it.createDate, LocalDate.now())
                        set(it.currentPlayer, null)
                        //userId will need to be changed to use the token value
                        set(it.userId, input.userId)
                }
        }
}