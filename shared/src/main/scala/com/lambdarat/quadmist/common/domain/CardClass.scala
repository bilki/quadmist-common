package com.lambdarat.quadmist.common.domain

import com.lambdarat.quadmist.common.domain.CardClass.Name
import io.chrisdavenport.fuuid.FUUID
import io.estatico.newtype.macros.newtype

/** Card class.
  *
  * @param name card name
  */
final case class CardClass(name: Name)

object CardClass {
  @newtype case class Id(toUUID: FUUID)
  @newtype case class Name(toStr: String)
}
