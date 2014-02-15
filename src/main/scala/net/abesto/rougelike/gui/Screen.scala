package net.abesto.rougelike.gui

import com.googlecode.lanterna.terminal.Terminal
import net.abesto.rougelike.{Tile, Level}

class Screen(term: Terminal) {
  val width = 160
  val height = 40
  val statusBar = new StatusBar(this, 39)

  term.clearScreen()
  term.setCursorVisible(false)
  statusBar.writeDivider()

  def foreground(c: Color) = c.applyAsForeground(term)

  def putString(x: Integer, y: Integer, s: String) {
    term.moveCursor(x, y)
    s foreach term.putCharacter
  }

  def putChar(x: Integer, y: Integer, c: Char) = putString(x, y, c.toString)

  def drawLevel(level: Level) {
    assert(level.tiles.length == width)
    assert(level.tiles(0).length == height - statusBar.height)
    for (
      x <- 0 until width;
      y <- 0 until height - statusBar.height
    ) {
      putChar(x, y, level.tiles(x)(y).char)
    }
  }

  def drawTile(tile: Tile) {
    putChar(tile.x, tile.y, tile.char)
  }

  val writeHealth = statusBar.writeHealth _
  val writeTurn = statusBar.writeTurn _
}
