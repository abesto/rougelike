package net.abesto.rougelike

abstract class Tile(_level: Level, _x: Int, _y: Int) {
  var creature: Option[Creature] = None
  def char: Char = creature map {_.char} getOrElse emptyChar

  def emptyChar: Char
  def isWalkable: Boolean
  val level = _level
  val x = _x
  val y = _y
}

class FloorTile(level: Level, x: Int, y: Int) extends Tile(level, x, y) {
  def isWalkable = true
  def emptyChar = '.'
}

class WallTile(level: Level, x: Int, y: Int) extends Tile(level, x, y) {
  def isWalkable = false
  def emptyChar = '#'
}
