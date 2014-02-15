package net.abesto.rougelike.levelfactory

import net.abesto.rougelike.{FloorTile, WallTile, Level}

object WalledEmptyLevelFactory {
  def create(w: Int, h: Int): Level = {
    val level = new Level(w, h)
    for (
      x <- 0 until w;
      y <- 0 until h
    ) {
      if (Seq(0, w-1).contains(x) || Seq(0, h-1).contains(y)) {
        level.tiles(x)(y) = new WallTile(level, x, y)
      } else {
        level.tiles(x)(y) = new FloorTile(level, x, y)
      }
    }
    level
  }
}
