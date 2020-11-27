package com.lambdarat.quadmist.common.domain

import com.lambdarat.quadmist.common.domain.Square.Block
import com.lambdarat.quadmist.common.util.DefaultSpec
import com.lambdarat.quadmist.common.util.ModelGens._

class BoardSpec extends DefaultSpec {

  "A board" when {
    implicit val boardSettings: Settings = Settings.default

    "created" should {
      "have the size specified" in {
        forAll { board: Board =>
          board.grid.length shouldBe boardSettings.boardSideSize.toInt
        }
      }

      "have a number of blocks less or equal than max blocks" in {
        forAll { board: Board =>
          val numBlocks: Int = board.grid.flatten.collect { case Block => 1 }.sum

          numBlocks should be <= boardSettings.boardMaxBlocks.toInt
        }
      }
    }

  }

}
