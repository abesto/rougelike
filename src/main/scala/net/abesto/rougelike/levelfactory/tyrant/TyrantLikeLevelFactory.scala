package net.abesto.rougelike.levelfactory.tyrant

import org.slf4j.LoggerFactory
import net.abesto.rougelike.{Tile, FloorTile, WallTile, Level}
import net.abesto.rougelike.levelfactory.tyrant.features.{Feature, FeatureGenerator, StraightCorridor, RectangularRoom}
import scala.util.Random

/**
 * Based on the algorithm laid out at http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm
 * Originally used by the rouge-like Tyrant
 *
 * Fill the whole map with solid earth
 * Dig out a single room in the centre of the map
 * Pick a wall of any room
 * Decide upon a new feature to build
 * See if there is room to add the new feature through the chosen wall
 * If yes, continue. If no, go back to step 3
 * Add the feature through the chosen wall
 * Go back to step 3, until the dungeon is complete
 * Add the up and down staircases at random points in map
 * Finally, sprinkle some monsters and items liberally over dungeon
 */
object TyrantLikeLevelFactory {
  val logger = LoggerFactory.getLogger(TyrantLikeLevelFactory.getClass)

  def withEntrance(gen: Tile => Feature, level: Level): Option[Feature] =
    Stream.continually{level.tiles(Random.nextInt(level.w))(Random.nextInt(level.h))}
      .take(7)
      .map{t => gen(t)}
      .find{_.fits}

  def create(w: Int, h: Int) = {
    logger.info("creating_level {} {}", w, h)
    val level = new Level(w, h)
    for ( x <- 0 until w; y <- 0 until h ) {
      level.tiles(x)(y) = new WallTile(level, x, y)
    }

    val width = Random.nextInt(20) + 10
    val height = Random.nextInt(10) + 5
    val left = Random.nextInt(w - width - 1)
    val top = Random.nextInt(h - height - 1)
    for (x <- left until left + width; y <- top until top + height) {
      level.tiles(x)(y) = new FloorTile(level, x, y)
    }

    for (i <- 1 until Math.sqrt(w.toDouble * h).toInt * 10) {
      withEntrance(FeatureGenerator.next, level).foreach{_.applyToLevel(level)}
    }

    level
  }
}
