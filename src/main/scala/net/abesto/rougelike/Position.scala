package net.abesto.rougelike

import net.abesto.rougelike.Direction.Direction

case class Position(x: Int, y: Int) {
  def +(d: Direction) = Direction.delta(d) match {case d => Position(x + d._1, y + d._2)}
}

