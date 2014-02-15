package net.abesto.rougelike

import net.abesto.rougelike.Direction._
import scala.util.Try

abstract class Tile(_level: Level, _x: Int, _y: Int) {
  var creature: Option[Creature] = None
  def char: Char = creature map {_.char} getOrElse emptyChar

  def emptyChar: Char
  def isWalkable: Boolean
  val level = _level
  val x = _x
  val y = _y

  def neighbor(d: Direction) = Try(level.tiles(x + delta(d)._1)(y + delta(d)._2)).toOption
  def neighbors: Set[Tile] = (Direction.values map neighbor).flatten
}

class FloorTile(level: Level, x: Int, y: Int) extends Tile(level, x, y) {
  def isWalkable = true
  def emptyChar = '.'

  override def toString = s"floor($x,$y)"
}

class WallTile(level: Level, x: Int, y: Int) extends Tile(level, x, y) {
  def isWalkable = false
  def emptyChar = '#'

  override def toString = s"wall($x,$y)"
}
