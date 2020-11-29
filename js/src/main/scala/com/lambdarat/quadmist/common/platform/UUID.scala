package com.lambdarat.quadmist.common.platform

import cats.Show
import io.circe.{Codec, Decoder, Encoder}
import io.estatico.newtype.macros.newtype

object UUID {
  @newtype final case class UUID(value: String)

  private val uuidRegex  =
    """[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}""".r
  implicit val uuidCodec = Codec.from[UUID](
    Decoder.decodeString
      .emap(str =>
        Either.cond(uuidRegex.matches(str), UUID(str), s"$str does not match UUID regex")
      ),
    Encoder.encodeString.contramap[UUID](_.value)
  )

  implicit val uuidShow = Show[UUID](_.value)

  // TODO This is supposed to be a random UUID
  val nextValue: UUID = UUID("7919293f-88b9-411e-9920-57bff4c5a8cf")

  def fromString(value: String): UUID = UUID(value)
}
