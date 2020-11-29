package com.lambdarat.quadmist.common.game

import com.lambdarat.quadmist.common.domain._

final case class GameMovement(
    color: Color,
    coords: Coordinates,
    cardId: Card.Id,
    target: Option[Arrow]
)

sealed trait GameEvent

object GameEvent {
  case class PlayerJoined(id: Player.Id)          extends GameEvent
  case class PlayerHand(initialHand: InitialHand) extends GameEvent
  case class PlayerMove(move: GameMovement)       extends GameEvent
  case class TurnTimeout(id: Player.Id)           extends GameEvent
  case object GameFinished                        extends GameEvent
}
