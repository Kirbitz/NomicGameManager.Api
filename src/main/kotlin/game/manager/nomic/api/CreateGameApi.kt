package nomic.game.manager.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Entity
data class Game(
        @PrimaryKey(autoGenerate = true)
        val gameId: Int,
        val title: String,

        @ColumnInfo(defaultValue = LocalDate.now())
        val createDate: String,

        @ColumnInfo(defaultValue = null)
        val currentPlayer: int,
)