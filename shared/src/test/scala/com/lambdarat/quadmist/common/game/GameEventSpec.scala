package com.lambdarat.quadmist.common.game

import cats.implicits._
import com.lambdarat.quadmist.common.codecs._
import com.lambdarat.quadmist.common.domain.Card
import com.lambdarat.quadmist.common.game.GameEvent._
import com.lambdarat.quadmist.common.platform.UUID._
import com.lambdarat.quadmist.common.util.DefaultSpec
import io.circe.DecodingFailure
import io.circe.parser._

class GameEventSpec extends DefaultSpec {

  "PlayerInitialHand" when {

    "decoding" should {
      "deserialize correctly" in {
        val rawEncoding =
          """
            |{"initialHand":{
            |"c1":"353dc0fe-e3d5-4d09-bcac-951d60865ea2",
            |"c2":"cd098048-3517-4f39-a25a-4a1e5f0c3e7e",
            |"c3":"12ed5740-d894-4f92-a0b6-0ae494acbdbc",
            |"c4":"c88ddb5f-a085-4607-8476-c9eb5618f3f4",
            |"c5":"b1ab1509-0c2b-404b-a968-2e49b90463d3"}
            |}
            |""".stripMargin

        val decoded = parse(rawEncoding).flatMap(_.as[PlayerHand])

        val expected = PlayerHand(
          InitialHand(
            c1 = Card.Id(fromString("353dc0fe-e3d5-4d09-bcac-951d60865ea2")),
            c2 = Card.Id(fromString("cd098048-3517-4f39-a25a-4a1e5f0c3e7e")),
            c3 = Card.Id(fromString("12ed5740-d894-4f92-a0b6-0ae494acbdbc")),
            c4 = Card.Id(fromString("c88ddb5f-a085-4607-8476-c9eb5618f3f4")),
            c5 = Card.Id(fromString("b1ab1509-0c2b-404b-a968-2e49b90463d3"))
          )
        ).asRight[DecodingFailure]

        decoded shouldBe expected
      }
    }
  }
}
