package com.lambdarat.quadmist.common.domain

import com.lambdarat.quadmist.common.domain.Coordinates.{XAxis, YAxis}
import enumeratum.EnumEntry.Uppercase
import enumeratum.{CirceEnum, Enum, EnumEntry}

/** Card arrows.
  *
  * Packed representation (bits):
  *
  *   N   NE  E   SE  S   SW  W   NW
  * ---------------------------------
  * | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
  * ---------------------------------
  */
sealed trait Arrow extends EnumEntry {
  def hex: Byte
  def opposite: Arrow
}

object Arrow extends Enum[Arrow] with CirceEnum[Arrow] {
  val values: IndexedSeq[Arrow] = findValues

  case object N  extends Arrow with Uppercase {
    val hex: Byte = 0x80.toByte; val opposite: Arrow = S
  }
  case object NE extends Arrow with Uppercase { val hex: Byte = 0x40; val opposite: Arrow = SW }
  case object E  extends Arrow with Uppercase { val hex: Byte = 0x20; val opposite: Arrow = W  }
  case object SE extends Arrow with Uppercase { val hex: Byte = 0x10; val opposite: Arrow = NW }
  case object S  extends Arrow with Uppercase { val hex: Byte = 0x08; val opposite: Arrow = N  }
  case object SW extends Arrow with Uppercase { val hex: Byte = 0x04; val opposite: Arrow = NE }
  case object W  extends Arrow with Uppercase { val hex: Byte = 0x02; val opposite: Arrow = E  }
  case object NW extends Arrow with Uppercase { val hex: Byte = 0x01; val opposite: Arrow = SE }

  val MAX_ARROWS: Int = values.size

  /** Checks whether arrow list does not contain repeated arrows and
    * does not exceed the number of max arrows for a card.
    *
    * @param arrows arrow list to be checked
    * @return true if arrow list is valid, false otherwise
    */
  def checkArrows(arrows: List[Arrow]): Boolean =
    arrows.distinct.size == arrows.size && arrows.size <= MAX_ARROWS

  /** Get the target coordinates of the arrow, given the origin of coordinates.
    *
    * @param arrow get coords for this arrow
    * @param origin center of coordinates
    * @return the coordinates of the arrow
    */
  def target(arrow: Arrow, origin: Coordinates): Coordinates = {
    val i = origin.x.toInt
    val j = origin.y.toInt

    val (newX, newY) = arrow match {
      case N  => (i - 1, j)
      case NE => (i - 1, j + 1)
      case E  => (i, j + 1)
      case SE => (i - 1, j + 1)
      case S  => (i - 1, j)
      case SW => (i - 1, j - 1)
      case W  => (i, j - 1)
      case NW => (i - 1, j - 1)
    }

    Coordinates(XAxis(newX), YAxis(newY))
  }

  /** Extract a list of arrows from a packed byte.
    *
    * @param packed a byte with packed arrows
    * @return a list with the arrows contained into the packed byte
    */
  def extract(packed: Byte): List[Arrow] =
    values.toList.filterNot(arrow => (arrow.hex & packed) == 0)

  /** Compresses a list of arrows into a packed byte.
    *
    * @param arrows list of arrows
    * <b>Precondition:</b>
    * arrows must be a list of distinct arrows, with a max size of [[MAX_ARROWS]]
    * @return some byte with the arrows compressed when preconditions are true
    */
  def compress(arrows: List[Arrow]): Option[Byte] =
    Option.when(checkArrows(arrows)) {
      if (arrows.isEmpty) 0x00 // Card with no arrows...
      else arrows.foldLeft[Byte](0x00)((total, next) => (total | next.hex).toByte)
    }
}
