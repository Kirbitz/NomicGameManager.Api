package nomic.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

class User(override val id: Int, val name : String) : IEntity {

}

@JvmInline
value class Username(@JsonValue val rawName: String) {
    init {
        TODO("Discuss and add business logic regarding valid usernames " +
                "(Probably alphanumeric with dashes and underlines only)")
    }
}