package com.lambdarat.quadmist.common.domain

import enumeratum.EnumEntry.Lowercase
import enumeratum.{CirceEnum, Enum, EnumEntry}

/** Possible colors inside game.
  */
sealed trait Color extends EnumEntry { def flip: Color }

object Color extends Enum[Color] with CirceEnum[Color] with Lowercase {
  val values = findValues

  case object Red  extends Color { def flip: Color = Blue }
  case object Blue extends Color { def flip: Color = Red  }
}
