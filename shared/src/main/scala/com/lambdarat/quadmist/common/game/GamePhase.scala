package com.lambdarat.quadmist.common.game

import enumeratum.EnumEntry.LowerCamelcase
import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait GamePhase extends EnumEntry

object GamePhase extends Enum[GamePhase] with CirceEnum[GamePhase] {
  val values = findValues

  case object Initial  extends GamePhase with LowerCamelcase
  case object BlueTurn extends GamePhase with LowerCamelcase
  case object RedTurn  extends GamePhase with LowerCamelcase
  case object Finish   extends GamePhase with LowerCamelcase
}
