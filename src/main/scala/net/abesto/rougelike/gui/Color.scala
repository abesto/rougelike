package net.abesto.rougelike.gui

import com.googlecode.lanterna.terminal.Terminal

object Color {
  val WHITE  = Color(255, 255, 255)
  val GREEN  = Color(0, 255, 0)
  val RED    = Color(255, 0, 0)
  val YELLOW = Color(255, 255, 0)
}

case class Color(r: Integer, g: Integer, b: Integer) {
  def applyAsForeground(t: Terminal) = t.applyForegroundColor(r, g, b)
  def applyAsBackground(t: Terminal) = t.applyBackgroundColor(r, g, b)
}


