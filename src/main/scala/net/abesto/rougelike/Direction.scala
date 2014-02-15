package net.abesto.rougelike

object Direction extends Enumeration {
  type Direction = Value
  val North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest = Value

  def delta(d: Direction) = d match {
    case North => (0, -1)
    case NorthEast => (1, -1)
    case East => (1, 0)
    case SouthEast => (1, 1)
    case South => (0, 1)
    case SouthWest => (-1, 1)
    case West => (-1, 0)
    case NorthWest => (-1, -1)
  }

  def reverse(d: Direction) = Direction((d.id + (Direction.values.size / 2)) % Direction.values.size)
}
