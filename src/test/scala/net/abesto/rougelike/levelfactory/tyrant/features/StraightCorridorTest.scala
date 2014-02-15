package net.abesto.rougelike.levelfactory.tyrant.features

import net.abesto.rougelike.{FloorTile, WallTile, Level}
import net.abesto.rougelike.Direction._

class StraightCorridorTest extends org.specs2.mutable.Specification {
  "direction" should {
    "be South if there's an open space to the north of the entrance" in {
      val level = new Level(10, 10)
      level.tiles(3)(3) = new WallTile(level, 3, 3)
      level.tiles(3)(2) = new FloorTile(level, 3, 2)
      new StraightCorridor(5, level.tiles(3)(3)).direction should be(South)
    }

    "be SouthEast if there's an open space to the north-west of the entrance" in {
      val level = new Level(10, 10)
      level.tiles(3)(3) = new WallTile(level, 3, 3)
      level.tiles(2)(2) = new FloorTile(level, 2, 2)
      new StraightCorridor(5, level.tiles(3)(3)).direction should be(SouthEast)
    }

    "throw an exception if there's no adjacent open space" in {
      val level = new Level(10, 10);
      { new StraightCorridor(5, level.tiles(3)(3)) } should throwA[Throwable]
    }
  }
}
