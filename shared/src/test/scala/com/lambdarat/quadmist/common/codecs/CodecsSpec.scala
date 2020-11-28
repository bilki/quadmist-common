package com.lambdarat.quadmist.common.codecs

import cats.implicits._
import com.lambdarat.quadmist.common.domain.Arrow._
import com.lambdarat.quadmist.common.domain.Square._
import com.lambdarat.quadmist.common.domain.{Arrow, Settings, Square}
import com.lambdarat.quadmist.common.util.DefaultSpec
import com.lambdarat.quadmist.common.util.ModelGens._
import io.circe.DecodingFailure
import io.circe.parser._
import io.circe.syntax._

class CodecsSpec extends DefaultSpec {
  private implicit val defaultSettings: Settings = Settings.default

  "Arrow codec" when {

    "encoding" should {
      "serialize as uppercase string the correct direction" in {
        forAll { arrow: Arrow =>
          val expected = arrow match {
            case N  => "N".asJson
            case NE => "NE".asJson
            case E  => "E".asJson
            case SE => "SE".asJson
            case S  => "S".asJson
            case SW => "SW".asJson
            case W  => "W".asJson
            case NW => "NW".asJson
          }

          arrow.asJson shouldBe expected
        }
      }
    }

    "decoding" should {
      "deserialize from uppercase string to the correct direction" in {
        val expected = Arrow.values.asRight[DecodingFailure]

        val rawArrows = """["N", "NE", "E", "SE", "S", "SW", "W", "NW"]"""

        val decodedArrows = parse(rawArrows).flatMap(_.as[List[Arrow]])

        decodedArrows shouldBe expected
      }
    }
  }

  "Square codec" when {

    "encoding" should {
      "serialize to string for block and free, and object for occupied" in {
        forAll { square: Square =>
          square match {
            case Block       => square.asJson shouldBe "block".asJson
            case Free        => square.asJson shouldBe "free".asJson
            case _: Occupied =>
              val occEncoding = square.asJson

              occEncoding.hcursor.downField("cclass").succeeded shouldBe true
              occEncoding.hcursor.downField("card").succeeded shouldBe true
              occEncoding.hcursor.downField("color").succeeded shouldBe true
          }
        }
      }
    }

    "decoding" should {
      "deserialize from string for block and free, and from object for occupied" in {
        "free".asJson.as[Square] shouldBe Free.asRight[DecodingFailure]
        "block".asJson.as[Square] shouldBe Block.asRight[DecodingFailure]

        forAll { occupied: Occupied =>
          occupied.asJson.as[Square] shouldBe occupied.asRight[DecodingFailure]
        }
      }
    }
  }

}
