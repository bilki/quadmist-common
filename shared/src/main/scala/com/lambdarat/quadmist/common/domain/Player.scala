package com.lambdarat.quadmist.common.domain

import com.lambdarat.quadmist.common.domain.Player.Name
import com.lambdarat.quadmist.common.platform.UUID._

import io.estatico.newtype.macros.newtype

/** A game player.
  *
  *  @param name name of this player
  */
final case class Player(name: Name)

object Player {
  @newtype case class Id(toUUID: UUID)
  @newtype case class Name(toStr: String)
}
