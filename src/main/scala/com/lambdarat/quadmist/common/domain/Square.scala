package com.lambdarat.quadmist.common.domain

import enumeratum.EnumEntry.Lowercase
import enumeratum.{Enum, EnumEntry}

/** Possible states of a square.
  */
sealed trait Square extends EnumEntry

object Square extends Enum[Square] {
  val values = findValues

  case object Block                                                   extends Square with Lowercase
  case object Free                                                    extends Square with Lowercase
  case class Occupied(cclass: CardClass.Id, card: Card, color: Color) extends Square
}
