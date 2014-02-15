package net.abesto.rougelike.levelfactory.tyrant.features

import net.abesto.rougelike._
import Direction._

case class RectangularRoom(w: Int, h: Int)(entrance: Tile) extends Feature(entrance) {
  override val supportedDirections = Set(North, East, South, West)

  def tiles: Seq[Tile] = {
    val (topLeftX, topLeftY) = direction match {
      case North => (entrance.x - w/2,     entrance.y - h + 1)
      case East  => (entrance.x,           entrance.y - h/2)
      case South => (entrance.x - w/2,     entrance.y)
      case West  => (entrance.x - w + 1,   entrance.y - h/2)
      case d => throw new RuntimeException("Unsupported direction " + d)
    }
    for (
      x <- Math.max(0, topLeftX) until Math.min(level.w, topLeftX + w);
      y <- Math.max(0, topLeftY) until Math.min(level.h, topLeftY + h);
      tile = level.tiles(x)(y)
    ) yield tile
  }
}
