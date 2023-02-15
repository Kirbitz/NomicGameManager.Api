package nomic.domain.entities

import com.fasterxml.jackson.annotation.JsonValue

class User(override val id: Int, val name : String) : IEntity {

}