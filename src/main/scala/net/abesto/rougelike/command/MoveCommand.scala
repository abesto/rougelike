package net.abesto.rougelike.command

import net.abesto.rougelike.{Tile, Creature}
import scala.util.Try

class MoveCommand(deltaX: Int, deltaY: Int) extends Command {
  def apply(c: Creature): Seq[Tile] = {
    val oldTile = c.tile
    Try(oldTile.level.tiles(oldTile.x + deltaX)(oldTile.y + deltaY)).map{ newTile =>
      newTile.isWalkable match {
        case true => c.tile = newTile; Seq(oldTile, newTile)
        case false => Seq.empty
      }
    }
      .recover{case ex: ArrayIndexOutOfBoundsException => Seq()}
      .get
  }
}

object MoveCommand {
  val north = new MoveCommand(0, -1)
  val northEast = new MoveCommand(-1, 1)
  val east = new MoveCommand(1, 0)
  val southEast = new MoveCommand(1, 1)
  val south = new MoveCommand(0, 1)
  val southWest = new MoveCommand(-1, 1)
  val west = new MoveCommand(-1, 0)
  val northWest = new MoveCommand(-1, -1)
}
