package net.abesto.rougelike.levelfactory

import net.abesto.rougelike.Position

object Rectangle {
  def apply(x: Int, y: Int, w: Int, h: Int): Rectangle = Rectangle(Position(x, y), Size(w, h))
}

case class Rectangle(topLeft: Position, size: Size) {
  val area = size.area
  def x = topLeft.x
  def y = topLeft.y
  def w = size.w
  def h = size.h
}

