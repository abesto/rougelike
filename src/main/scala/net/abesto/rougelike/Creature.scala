package net.abesto.rougelike

abstract class Creature(var _tile: Tile) {
  tile = _tile  // Move to the initial position

  def tile = _tile
  def tile_=(t: Tile) {
    assert(t.creature.isEmpty)
    _tile.creature = None
    t.creature = Some(this)
    _tile = t
  }

  def char: Char
}

class Player(tile: Tile) extends Creature(tile) {
  def char = '@'
}

