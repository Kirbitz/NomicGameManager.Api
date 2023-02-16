package nomic.domain.entities

class User(val name: String, id: Int = -1) : Entity {

    override var id : Int = -1
        internal set

    init {
        this.id = id
    }
}
