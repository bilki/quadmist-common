package com.lambdarat.quadmist.common.platform

object UUID {
  type UUID = memeid4s.UUID

  implicit val showInstance    = memeid4s.cats.implicits.UUIDShowInstance
  implicit val decoderInstance = memeid4s.circe.instances.UUIDDecoderInstance
  implicit val encoderInstance = memeid4s.circe.instances.UUIDEncoderInstance

  def nextValue: UUID = memeid4s.UUID.V4.random
}
