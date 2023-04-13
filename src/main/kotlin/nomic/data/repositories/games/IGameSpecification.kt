package nomic.data.repositories.games

import nomic.data.IDatabaseSpecification
import nomic.data.dtos.GameDTO
import nomic.data.dtos.Games
import nomic.domain.ISpecification

fun interface IGameSpecification : ISpecification, IDatabaseSpecification<GameDTO, Games>
