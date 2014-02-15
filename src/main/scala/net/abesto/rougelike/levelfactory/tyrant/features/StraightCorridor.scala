package net.abesto.rougelike.levelfactory.tyrant.features

import net.abesto.rougelike.{WallTile, FloorTile, Direction, Tile}
import Direction._
import scala.util.Try

case class StraightCorridor(length: Int)(start: Tile) extends Feature(start) {
  override def tiles: Seq[Tile] = Stream.iterate(start){_.neighbor(direction).get}.take(length)

  override def toString = s"StraightCorridor($length,$start,${Try(direction).getOrElse("N/A")})"

  override val supportedDirections: Set[Direction.Direction] = Direction.values
}
