package com.lambdarat.quadmist.common

import cats.implicits._
import com.lambdarat.quadmist.common.domain.Card.{MagicalDef, PhysicalDef, Power}
import com.lambdarat.quadmist.common.domain.Coordinates.{XAxis, YAxis}
import com.lambdarat.quadmist.common.domain.Fight.{AttackerPoints, AttackerWins, DefenderPoints}
import com.lambdarat.quadmist.common.domain.Settings.{
  BoardMaxBlocks,
  BoardSize,
  CardMaxLevel,
  MaxHandCards
}
import com.lambdarat.quadmist.common.domain.Square.{Block, Free, Occupied}
import com.lambdarat.quadmist.common.domain._
import com.lambdarat.quadmist.common.game.GameEvent.{
  PlayerHand,
  PlayerMove,
  PlayerJoined,
  TurnTimeout,
  GameFinished
}
import com.lambdarat.quadmist.common.game._
import com.lambdarat.quadmist.common.platform.UUID
import com.lambdarat.quadmist.common.platform.UUID._
import io.circe.generic.semiauto.deriveCodec
import io.circe.syntax._
import io.circe.{Codec, Decoder, Encoder}

package object codecs {
  implicit val powerCodec       = Codec.from[Power](
    Decoder.decodeInt.map(Power.apply),
    Encoder.encodeInt.contramap[Power](_.toInt)
  )
  implicit val physicalDefCodec = Codec.from[PhysicalDef](
    Decoder.decodeInt.map(PhysicalDef.apply),
    Encoder.encodeInt.contramap[PhysicalDef](_.toInt)
  )
  implicit val magicalDefCodec  = Codec.from[MagicalDef](
    Decoder.decodeInt.map(MagicalDef.apply),
    Encoder.encodeInt.contramap[MagicalDef](_.toInt)
  )
  implicit val cardCodec        = deriveCodec[Card]

  implicit val xaxisCodec       = Codec.from[XAxis](
    Decoder.decodeInt.map(XAxis.apply),
    Encoder.encodeInt.contramap[XAxis](_.toInt)
  )
  implicit val yaxisCodec       = Codec.from[YAxis](
    Decoder.decodeInt.map(YAxis.apply),
    Encoder.encodeInt.contramap[YAxis](_.toInt)
  )
  implicit val coordinatesCodec = deriveCodec[Coordinates]

  implicit val attackerCodec       = deriveCodec[Attacker]
  implicit val defenderCodec       = deriveCodec[Defender]
  implicit val attackerPointsCodec = Codec.from[AttackerPoints](
    Decoder.decodeInt.map(AttackerPoints.apply),
    Encoder.encodeInt.contramap[AttackerPoints](_.toInt)
  )
  implicit val defenderPointsCodec = Codec.from[DefenderPoints](
    Decoder.decodeInt.map(DefenderPoints.apply),
    Encoder.encodeInt.contramap[DefenderPoints](_.toInt)
  )
  implicit val attackerWinsCodec   = Codec.from[AttackerWins](
    Decoder.decodeBoolean.map(AttackerWins.apply),
    Encoder.encodeBoolean.contramap[AttackerWins](_.toBool)
  )
  implicit val fightCodec          = deriveCodec[Fight]

  implicit val boardSizeCodec      = Codec.from[BoardSize](
    Decoder.decodeInt.map(BoardSize.apply),
    Encoder.encodeInt.contramap[BoardSize](_.toInt)
  )
  implicit val boardMaxBlocksCodec = Codec.from[BoardMaxBlocks](
    Decoder.decodeInt.map(BoardMaxBlocks.apply),
    Encoder.encodeInt.contramap[BoardMaxBlocks](_.toInt)
  )
  implicit val cardMaxLevelCodec   = Codec.from[CardMaxLevel](
    Decoder.decodeInt.map(CardMaxLevel.apply),
    Encoder.encodeInt.contramap[CardMaxLevel](_.toInt)
  )
  implicit val maxHandCardsCodec   = Codec.from[MaxHandCards](
    Decoder.decodeInt.map(MaxHandCards.apply),
    Encoder.encodeInt.contramap[MaxHandCards](_.toInt)
  )
  implicit val settingsCodec       = deriveCodec[Settings]

  implicit val cardClassIdCodec = Codec.from[CardClass.Id](
    Decoder[UUID].map(CardClass.Id.apply),
    Encoder.encodeString.contramap[CardClass.Id](_.toUUID.show)
  )
  implicit val occupiedCodec    = deriveCodec[Occupied]

  implicit val squareDecoder: Decoder[Square] = Decoder.decodeString
    .emap(str => Square.withNameLowercaseOnlyEither(str).leftMap(_.notFoundName))
    .or(Decoder[Occupied].widen)
  implicit val squareEncoder                  = Encoder.instance[Square] {
    case Free               => Free.entryName.asJson
    case Block              => Block.entryName.asJson
    case occupied: Occupied => occupied.asJson
  }
  implicit val squareCodec: Codec[Square]     = Codec.from[Square](squareDecoder, squareEncoder)

  implicit val boardCodec = deriveCodec[Board]

  implicit val cardClassNameCodec = Codec.from[CardClass.Name](
    Decoder.decodeString.map(CardClass.Name.apply),
    Encoder.encodeString.contramap[CardClass.Name](_.toStr)
  )

  implicit val playerIdCodec = Codec.from[Player.Id](
    Decoder[UUID].map(Player.Id.apply),
    Encoder.encodeString.contramap[Player.Id](_.toUUID.show)
  )
  implicit val cardIdCodec   = Codec.from[Card.Id](
    Decoder[UUID].map(Card.Id.apply),
    Encoder.encodeString.contramap[Card.Id](_.toUUID.show)
  )

  implicit val initialHandCodec       = deriveCodec[InitialHand]
  implicit val playerRequestHandCodec = deriveCodec[PlayerHand]

  implicit val playerJoinedCodec   = deriveCodec[PlayerJoined]
  implicit val playerGameMoveCodec = deriveCodec[GameMovement]
  implicit val playerMovementCodec = deriveCodec[PlayerMove]
  implicit val turnTimeoutCodec    = deriveCodec[TurnTimeout]
  implicit val gameFinishedCodec   = Codec.from[GameFinished.type](
    Decoder.decodeString.emap(str =>
      Either.cond(str.equals("finished"), GameFinished, "Not finished")
    ),
    Encoder.encodeString.contramap(_ => "finished")
  )
  implicit val gameEventCodec      = Codec.from[GameEvent](
    List[Decoder[GameEvent]](
      Decoder[PlayerMove].widen,
      Decoder[PlayerHand].widen,
      Decoder[PlayerJoined].widen,
      Decoder[TurnTimeout].widen,
      Decoder[GameFinished.type].widen
    ).reduceLeft(_ or _),
    Encoder.instance {
      case pj: PlayerJoined => pj.asJson
      case ph: PlayerHand   => ph.asJson
      case pm: PlayerMove   => pm.asJson
      case tt: TurnTimeout  => tt.asJson
      case GameFinished     => GameFinished.asJson
    }
  )

  implicit val gameTurnCodec  = deriveCodec[TurnFights]
  implicit val turnStateCodec = deriveCodec[TurnState]

  implicit val gameErrorCodec      = Encoder.encodeString.contramap[GameError](_.msg)
  implicit val eventOutcomeEncoder = Encoder.encodeEither[GameError, TurnState]("error", "turn")
}
