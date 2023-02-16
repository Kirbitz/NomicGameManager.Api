package nomic.domain.entities

interface Entity {
    val id: Int

    fun isPersisted() : Boolean = id > 0
}
