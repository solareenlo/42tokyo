import math._
import scala.util._
import scala.io.StdIn._
import scala.collection.immutable.ListMap

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle to grab it and use your team id to determine towards where you need to throw it.
 * Use the Wingardium spell to move things around at your leisure, the more magic you put it, the further they'll move.
 **/
object Player extends App {
  val myTeamId = readLine.toInt // if 0 you need to score on the right of the map, if 1 you need to score on the left
  val coorOfGoal = if (myTeamId == 0) Coordinate(99, 16000, 3750) else Coordinate(99, 0, 3750)
  val dist = (a: Coordinate) => ((b: Coordinate) => (b.id -> sqrt(pow(a.x - b.x, 2.0) + pow(a.y - b.y, 2.0))))
  val distToSnaffles = (snaffles: IndexedSeq[Coordinate], wizard: Coordinate) => snaffles.map(s => dist(wizard)(s))
  val indexesOfTargets = (distToSnaffles: IndexedSeq[(Int, Double)]) => ListMap(distToSnaffles.toSeq.sortBy(_._2):_*)
  val indexOfTarget = (indexes: ListMap[Int, Double]) => indexes.keys.head
  val coorOfTarget = (coorOfSnaffles: IndexedSeq[Coordinate], index: Int) => coorOfSnaffles.find(x => x.id == index) match {
      case Some(x) => x
  }

  // game loop
  while(true) {
    val Array(myScore, myMagic) = readLine().split(" ").map(_.toInt)
    val Array(opponentScore, opponentMagic) = readLine().split(" ").map(_.toInt)
    val entities = readLine.toInt // number of entities still in game
    val data = for (i <- 0 until entities) yield readLine().split(" ")
    val wizards = for (u <- data if u(1) == "WIZARD") yield new Wizard(u)
    val coorOfWizards = wizards.map(u => Coordinate(u.entityId, u.x, u.y))
    val opponentWizards = for (u <- data if u(1) == "OPPONENT_WIZARD") yield new OpponentWizard(u)
    val snaffles = for (u <- data if u(1) == "SNAFFLE") yield new Snaffle(u)
    val coorOfSnaffles = snaffles.map(u => Coordinate(u.entityId, u.x, u.y))
    val bludgers = for (u <- data if u(1) == "BLUDGER") yield new Bludger(u)

    val distToSnaffles0 = distToSnaffles(coorOfSnaffles, coorOfWizards(0))
    val indexesOfTargets0 = indexesOfTargets(distToSnaffles0)
    var indexOfTarget0 = indexOfTarget(indexesOfTargets0)
    var coorOfTarget0 = coorOfTarget(coorOfSnaffles, indexOfTarget0)

    val distToSnaffles1 = distToSnaffles(coorOfSnaffles, coorOfWizards(1))
    val indexesOfTargets1 = indexesOfTargets(distToSnaffles1)
    var indexOfTarget1 = indexOfTarget(indexesOfTargets1)
    var coorOfTarget1 = coorOfTarget(coorOfSnaffles, indexOfTarget1)

    if (indexOfTarget0 == indexOfTarget1 && indexesOfTargets1.size != 1) {
      val distToTarget0 = Map(dist(coorOfWizards(0))(coorOfTarget0))(indexOfTarget0)
      val distToTarget1 = Map(dist(coorOfWizards(1))(coorOfTarget1))(indexOfTarget1)
      val indexOfNextTarget0 = indexOfTarget(indexesOfTargets0.tail)
      val coorOfNextTarget0 = coorOfTarget(coorOfSnaffles, indexOfNextTarget0)
      val distToNextTarget0 = Map(dist(coorOfWizards(0))(coorOfNextTarget0))(indexOfNextTarget0)
      val indexOfNextTarget1 = indexOfTarget(indexesOfTargets1.tail)
      val coorOfNextTarget1 = coorOfTarget(coorOfSnaffles, indexOfNextTarget1)
      val distToNextTarget1 = Map(dist(coorOfWizards(1))(coorOfNextTarget1))(indexOfNextTarget1)

      if (distToTarget0 >= distToTarget1) {
        if ((distToTarget1 + distToNextTarget0) >= (distToNextTarget1 + distToTarget0)) {
          indexOfTarget1 = indexOfNextTarget1
          coorOfTarget1 = coorOfNextTarget1
        } else {
          indexOfTarget0 = indexOfNextTarget0
          coorOfTarget0 = coorOfNextTarget0
        }
      } else {
        if ((distToTarget1 + distToNextTarget0) <= (distToNextTarget1 + distToTarget0)) {
          indexOfTarget0 = indexOfNextTarget0
          coorOfTarget0 = coorOfNextTarget0
        } else {
          indexOfTarget1 = indexOfNextTarget1
          coorOfTarget1 = coorOfNextTarget1
        }
      }
    }

    if (wizards(0).state == 1) {
      if (abs(coorOfGoal.x - coorOfTarget0.x) > 8000) {
        if (myTeamId == 0) {
          println(s"THROW ${coorOfTarget0.x + 1000} ${coorOfTarget0.y} 500")
        } else {
          println(s"THROW ${coorOfTarget0.x - 1000} ${coorOfTarget0.y} 500")
        }
      } else {
        println(s"THROW ${coorOfGoal.x} ${coorOfGoal.y} 500")
      }
    } else {
      if (indexesOfTargets0.size == 1 && myMagic >= 33) {
        println(s"WINGARDIUM $indexOfTarget0 ${coorOfGoal.x} ${coorOfGoal.y} 33")
      } else {
        println(s"MOVE ${coorOfTarget0.x} ${coorOfTarget0.y} 150")
      }
    }

    if (wizards(1).state == 1) {
      if (abs(coorOfGoal.x - coorOfTarget1.x) > 8000) {
        if (myTeamId == 0) {
          println(s"THROW ${coorOfTarget1.x + 1000} ${coorOfTarget1.y} 500")
        } else {
         println(s"THROW ${coorOfTarget1.x - 1000} ${coorOfTarget1.y} 500")
        }
      } else {
        println(s"THROW ${coorOfGoal.x} ${coorOfGoal.y} 500")
      }
    } else {
      println(s"MOVE ${coorOfTarget1.x} ${coorOfTarget1.y} 150")
    }
  }
}

    // Write an action using println
    // To debug: Console.err.println("Debug messages...")
    // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤ power ≤ 500, 0 ≤ magic ≤ 1500)
    // i.e.: "MOVE x y thrust" or "THROW x y power" or "WINGARDIUM id x y magic"

    // for(i <- 0 until entities) {
    //   val Array(_entityId, entityType, _x, _y, _vx, _vy, _state) = readLine().split(" ")
    //   val entityId = _entityId.toInt
    //   val x = _x.toInt
    //   val y = _y.toInt
    //   val vx = _vx.toInt
    //   val vy = _vy.toInt
    //   val state = _state.toInt
    // }

case class Coordinate(id: Int, x: Int, y: Int)

trait User {
  // entityId: entity identifier
  // entityType: "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" or "BLUDGER"
  // x: position
  // y: position
  // vx: velocity
  // vy: velocity
  // state: 1 if the wizard is holding a Snaffle, 0 otherwise. 1 if the Snaffle is being held, 0 otherwise. id of the last victim of the bludger.
  val entityId: Int
  val entityType: String
  val x: Int
  val y: Int
  val vx: Int
  val vy: Int
  val state: Int
}

class Wizard(u: Array[String]) extends User {
  val entityId = u(0).toInt
  val entityType = u(1)
  val x = u(2).toInt
  val y = u(3).toInt
  val vx = u(4).toInt
  val vy = u(5).toInt
  val state = u(6).toInt
}

class OpponentWizard(u: Array[String]) extends User {
  val entityId = u(0).toInt
  val entityType = u(1)
  val x = u(2).toInt
  val y = u(3).toInt
  val vx = u(4).toInt
  val vy = u(5).toInt
  val state = u(6).toInt
}

class Snaffle(u: Array[String]) extends User {
  val entityId = u(0).toInt
  val entityType = u(1)
  val x = u(2).toInt
  val y = u(3).toInt
  val vx = u(4).toInt
  val vy = u(5).toInt
  val state = u(6).toInt
}

class Bludger(u: Array[String]) extends User {
  val entityId = u(0).toInt
  val entityType = u(1)
  val x = u(2).toInt
  val y = u(3).toInt
  val vx = u(4).toInt
  val vy = u(5).toInt
  val state = u(6).toInt
}
