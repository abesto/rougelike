package net.abesto.rougelike.levelfactory.tyrant.features

import scala.util.Random
import net.abesto.rougelike.Tile

object FeatureGenerator {
  def next: (Tile => Feature) = {
    val r = Random.nextFloat()
    if (r < .2) {
      return StraightCorridor(Random.nextInt(40))
    }
    RectangularRoom(Random.nextInt(15), Random.nextInt(15))
  }
}
