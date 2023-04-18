package nomic.data.repositories.games

import nomic.data.IDatabaseSpecification
import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import nomic.domain.ISpecification

/**
 * This abstraction unites both the specification pattern and the database to offer an interface
 * for applying inheriting specifications to games specifically.
 */
fun interface IGameSpecification : ISpecification, IDatabaseSpecification<GameDTO, Games>
