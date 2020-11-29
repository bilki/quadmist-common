package com.lambdarat.quadmist.common.platform

import cats.implicits._
import com.lambdarat.quadmist.common.platform.UUID._
import com.lambdarat.quadmist.common.util.DefaultSpec
import io.circe.DecodingFailure
import io.circe.parser._
import io.circe.syntax._

class UuidSpec extends DefaultSpec {

  // Mainly for testing the regex decoding
  "UUID codec" when {

    "converting to and from json" should {
      "serialize as a simple string" in {
        val uuid = UUID("7d588a91-c7a1-4a02-8f78-c3929c119849")

        uuid.asJson shouldBe "7d588a91-c7a1-4a02-8f78-c3929c119849".asJson
      }

      "deserialize from a simple string" in {
        val uuidValue   = UUID("7d588a91-c7a1-4a02-8f78-c3929c119849").asRight[DecodingFailure]
        val decodedUUID = parse("\"7d588a91-c7a1-4a02-8f78-c3929c119849\"").flatMap(_.as[UUID])

        uuidValue shouldBe decodedUUID
      }

      "serialize and deserialize correctly from the same value" in {
        val originalValue = UUID("7d588a91-c7a1-4a02-8f78-c3929c119849")
        val encodedUUID   = originalValue.asJson
        val decodedUUID   = parse(encodedUUID.noSpaces).flatMap(_.as[UUID])

        originalValue.asRight[DecodingFailure] shouldBe decodedUUID
      }

      "fail when the uuid does not match the regex" in {
        val nonCompliantUUID = "\"7d588a91-c7a1-4a02-8f78-c3929c11984X\"" // tailing X
        val decodedUUID      = parse(nonCompliantUUID).flatMap(_.as[UUID])

        decodedUUID match {
          case Left(_)      => succeed
          case Right(value) => fail(s"Should not be decoding the value ${value.value}")
        }
      }
    }

  }

}
