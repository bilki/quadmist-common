package com.lambdarat.quadmist.common.game

import com.lambdarat.quadmist.common.domain.Fight
import io.chrisdavenport.fuuid.FUUID
import io.estatico.newtype.macros.newtype

import java.time.Instant

/** Actions that happened during one game turn.
  *
  * @param fights fights for this turn
  * @param playedAt instant when game turn was played
  */
case class TurnFights(
    fights: List[Fight],
    playedAt: Instant
)

object TurnFights {
  @newtype case class Id(toUUID: FUUID)
}
