package net.abesto.rougelike.gui

import net.abesto.rougelike.Health
import scala.collection.immutable.TreeMap

class StatusBar(screen: Screen, y: Int) {
  val healthStart = 0
  val turnStart = 20
  val height = 2

  def writeDivider() {
    screen.foreground(Color.WHITE)
    screen.putString(0, y-1, "_" * screen.width)
  }

  def writeHealth(current: Health, max: Health) {
    val prefix = "Health: "
    screen.foreground(Color.WHITE)
    screen.putString(0, y, prefix)
    screen.foreground(current.colorCurrent(max, TreeMap(0.3 -> Color.RED, 0.8 -> Color.YELLOW), Color.GREEN))
    screen.putString(healthStart + prefix.length, y, current.value.toString)
    screen.foreground(Color.WHITE)
    screen.putString(healthStart + current.value.toString.length + prefix.length, y, s"/${max.value}")
  }

  def writeTurn(n: Int) {
    val prefix = "Turn: "
    screen.foreground(Color.WHITE)
    screen.putString(turnStart, y, prefix + n)
  }
}
