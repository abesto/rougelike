package net.abesto.rougelike.levelfactory.bsp

import net.abesto.rougelike.levelfactory.Rectangle

case class BspTree(rect: Rectangle, parent: Option[BspTree], var children: (Option[BspTree], Option[BspTree]) = (None, None)) {
  def left = children._1
  def right = children._2

  def left_=(tree: BspTree) = children = (Some(tree), right)
  def right_=(tree: BspTree) = children = (left, Some(tree))

  def isLeaf = left.isEmpty && right.isEmpty

  def x = rect.topLeft.x
  def y = rect.topLeft.y
  def w = rect.size.w
  def h = rect.size.h

  var splitDir: Option[SplitDir.SplitDir] = None

  override def toString = s"BSP($rect,${splitDir.map{_.toString()(0)}.getOrElse("?")}"

  def depth: Int = if (isLeaf) 0 else 1 + Math.max(left.get.depth, right.get.depth)
}
