package net.abesto.rougelike.levelfactory.tyrant.features

import net.abesto.rougelike._
import Direction._
import org.slf4j.LoggerFactory
import scala.util.{Random, Try}
import net.abesto.rougelike.Level

abstract class Feature(entrance: Tile) {
  val logger = LoggerFactory.getLogger(this.getClass)
  val level = entrance.level

  def blockingTiles: Seq[Tile] = tiles
    .map{_.neighbors}.flatten
    .++(tiles)
    .filter{_.isInstanceOf[FloorTile]}
    .filter{!entrance.neighbors.+(entrance).contains(_)}

  val supportedDirections: Set[Direction.Direction]

  lazy val direction: Direction.Direction = Random.shuffle(supportedDirections)
    .find{d => entrance.neighbor(d).exists{_.isInstanceOf[WallTile]} && entrance.neighbor(reverse(d)).exists{_.isInstanceOf[FloorTile]}}
    .get

  def fits: Boolean = Try(blockingTiles match {
    case Seq() =>
      logger.info("{} fits", this)
      true
    case tiles =>
      logger.info(s"$this blocked_by ${tiles.mkString(", ")}")
      false
  }).recover{case e =>
    logger.info(s"$this failed $e")
    false
  }.get

  def applyToLevel(level: Level): Unit = {
    logger.info("apply {}", this)
    tiles foreach {t => level.tiles(t.x)(t.y) = new FloorTile(level, t.x, t.y)}
  }

  def tiles: Seq[Tile]
}
