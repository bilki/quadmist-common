package com.lambdarat.quadmist.common.util

import com.lambdarat.quadmist.common.domain.BattleClass.{Assault, Flexible, Magical, Physical}
import com.lambdarat.quadmist.common.domain.Board.Hand
import com.lambdarat.quadmist.common.domain.Card.{MagicalDef, PhysicalDef, Power}
import com.lambdarat.quadmist.common.domain.Color.{Blue, Red}
import com.lambdarat.quadmist.common.domain.Square.{Block, Free, Occupied}
import com.lambdarat.quadmist.common.domain._
import com.lambdarat.quadmist.common.utils.BoardGenerator
import com.lambdarat.quadmist.common.platform.UUID
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

object ModelGens {
  private val battleClassGen: Gen[BattleClass]        =
    Gen.oneOf(Physical, Magical, Flexible, Assault)
  implicit val battleClassArb: Arbitrary[BattleClass] = Arbitrary(battleClassGen)

  private val arrowsGen: Gen[List[Arrow]] = Gen.someOf(Arrow.values).map(_.toList)

  val invalidArrowsGen: Gen[List[Arrow]] = Gen.choose(1, Arrow.MAX_ARROWS + 1) flatMap { size =>
    Gen.listOfN(size, Gen.oneOf(Arrow.values))
  }

  implicit val arrowsArb: Arbitrary[List[Arrow]] = Arbitrary(arrowsGen)

  private val arrowGen: Gen[Arrow]        = Gen.oneOf(Arrow.values)
  implicit val arrowArb: Arbitrary[Arrow] = Arbitrary(arrowGen)

  def unwrapOptGen[A](opt: Option[A]): Gen[A] = opt.fold[Gen[A]](Gen.fail)(Gen.const)

  private def cardGen(implicit gameSettings: Settings): Gen[Card] = {
    val maxLevel = gameSettings.cardMaxLevel.toInt

    for {
      power       <- Gen.choose(0, maxLevel - 1).map(Power.apply)
      battleClass <- arbitrary[BattleClass]
      pdef        <- Gen.choose(0, maxLevel - 1).map(PhysicalDef.apply)
      mdef        <- Gen.choose(0, maxLevel - 1).map(MagicalDef.apply)
      arrows      <- arbitrary[List[Arrow]]
      maybeCard    = Card.create(power, battleClass, pdef, mdef, arrows)
      card        <- unwrapOptGen(maybeCard)
    } yield card
  }

  implicit def cardArb(implicit gameSettings: Settings): Arbitrary[Card] =
    Arbitrary(cardGen)

  private def handGen(implicit gameSettings: Settings): Gen[Hand] =
    Gen.containerOfN[Set, Card](gameSettings.maxHandCards.toInt, cardGen)

  private def boardGen(implicit gameSettings: Settings): Gen[Board] =
    for {
      redHand  <- handGen
      blueHand <- handGen
    } yield BoardGenerator.random(redHand, blueHand, gameSettings)

  implicit def boardArb(implicit gameSettings: Settings): Arbitrary[Board] = Arbitrary(boardGen)

  private val playerIdGen: Gen[Player.Id]        = Gen.const(Player.Id(UUID.nextValue))
  implicit val playerIdArb: Arbitrary[Player.Id] = Arbitrary(playerIdGen)

  private val cardClassIdGen: Gen[CardClass.Id]        = Gen.const(CardClass.Id(UUID.nextValue))
  implicit val cardClassIdArb: Arbitrary[CardClass.Id] = Arbitrary(cardClassIdGen)

  private val colorGen: Gen[Color]        = Gen.oneOf(Red, Blue)
  implicit val colorArb: Arbitrary[Color] = Arbitrary(colorGen)

  private def occupiedGen(implicit gameSettings: Settings): Gen[Occupied]        = for {
    cardClassId <- arbitrary[CardClass.Id]
    card        <- cardGen
    color       <- arbitrary[Color]
  } yield Occupied(cardClassId, card, color)
  implicit def occupiedArb(implicit gameSettings: Settings): Arbitrary[Occupied] =
    Arbitrary(occupiedGen)

  private def squareGen(implicit gameSettings: Settings): Gen[Square]        =
    Gen.oneOf(Gen.const(Free), Gen.const(Block), occupiedGen)
  implicit def squareArb(implicit gameSettings: Settings): Arbitrary[Square] =
    Arbitrary(squareGen)
}
