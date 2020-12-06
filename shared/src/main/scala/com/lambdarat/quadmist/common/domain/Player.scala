package com.lambdarat.quadmist.common.domain

import com.lambdarat.quadmist.common.domain.Player.Name
import io.chrisdavenport.fuuid.FUUID
import io.estatico.newtype.macros.newtype

/** A game player.
  *
  *  @param name name of this player
  */
final case class Player(name: Name)

object Player {
  @newtype case class Id(toUUID: FUUID)
  @newtype case class Name(toStr: String)
}
