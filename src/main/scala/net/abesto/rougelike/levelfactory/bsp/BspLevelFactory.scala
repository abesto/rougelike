package net.abesto.rougelike.levelfactory.bsp

import net.abesto.rougelike.levelfactory.{LevelFactory, Rectangle, Size}
import net.abesto.rougelike._
import scala.util.{Try, Random}
import org.slf4j.LoggerFactory
import net.abesto.rougelike.Direction.Direction
import net.abesto.rougelike.Position
import net.abesto.rougelike.levelfactory.bsp.BspTree
import scala.Some
import net.abesto.rougelike.Level
import net.abesto.rougelike.levelfactory.Size
import scala.collection.immutable.IndexedSeq

class BspLevelFactory(theme: BspTheme) extends LevelFactory {
  val logger = LoggerFactory.getLogger(this.getClass)

  def maxLeafArea(mapSize: Size) = 1200

  def levelDown(trees: Seq[BspTree]): Seq[BspTree] = trees
    .filterNot{_.isLeaf}
    .map{t => Seq(t.left.get, t.right.get)}.flatten

  def levelUp(trees: Seq[BspTree]): Seq[BspTree] = trees.filterNot{_.parent.isEmpty}.map{_.parent}.flatten

  def leaves(tree: BspTree): Seq[BspTree] = tree.children match {
    case (None, None)    => Seq(tree)
    case (None, Some(t)) => leaves(t)
    case (Some(t), None) => leaves(t)
    case (Some(l), Some(r)) => leaves(l) ++ leaves(r)
  }

  def nodesAtDepth(d: Int, t: BspTree): Seq[BspTree] =
    if (d == 0) Seq(t)
    else if (t.isLeaf) Seq[BspTree]()
    else nodesAtDepth(d-1, t.left.get) ++ nodesAtDepth(d-1, t.right.get)

  def areLeavesSmallerThan(area: Int, tree: BspTree): Boolean = leaves(tree).find{_.rect.area > area}.isEmpty

  def splitLeaf(minSplitSize: Int, splitRatioDeviation: Float)(tree: BspTree) = {
    val dir = SplitDir(Random.nextInt(SplitDir.values.size))
    val size = if (dir == SplitDir.Horizontal) tree.rect.size.w else tree.rect.size.h
    val minRatio = 0.5f - splitRatioDeviation
    val maxRatio = 0.5f + splitRatioDeviation
    val ratio = minRatio + (Random.nextDouble() * (maxRatio - minRatio))
    val offset = Math.max(minSplitSize, size * ratio).round.toInt
    if (offset >= minSplitSize && size - offset >= minSplitSize) {
      tree.splitDir = Some(dir)
      if (dir == SplitDir.Horizontal) {
        tree.children = (
          Some(BspTree(Rectangle(tree.rect.topLeft, Size(offset, tree.h)), Some(tree))),
          Some(BspTree(Rectangle(Position(tree.x + offset, tree.y), Size(tree.w - offset, tree.h)), Some(tree))))
      } else {
        tree.children = (
          Some(BspTree(Rectangle(tree.rect.topLeft, Size(tree.w, offset)), Some(tree))),
          Some(BspTree(Rectangle(Position(tree.x, tree.y + offset), Size(tree.w, tree.h - offset)), Some(tree))))
      }
    }
  }

  def tilesInRectangle(level: Level, rect: Rectangle) =
    for (x <- rect.x until rect.x + rect.w;
         y <- rect.y until rect.y + rect.h;
         tile = level.tiles(x)(y)
    ) yield tile

  // Wheee, ray tracing!
  def findEdges(level: Level, rect: Rectangle, dir: Direction) = {
    val startPositions = dir match {
      case Direction.East => for (y <- rect.y until rect.y + rect.h) yield Position(rect.x + rect.w, y)
      case Direction.South => for (x <- rect.x until rect.x + rect.w) yield Position(x, rect.y + rect.h)
      case Direction.West => for (y <- rect.y until rect.y + rect.h) yield Position(rect.x, y)
      case Direction.North => for (x <- rect.x until rect.x + rect.w) yield Position(x, rect.y)
    }
    val rayDir = Direction.reverse(dir)
    startPositions.map{p =>
      Stream.iterate(p){_ + rayDir}
        .takeWhile{p => Try(level.tiles(p.x)(p.y)).isSuccess}
        .map{p => level.tiles(p.x)(p.y)}
        .find{t => t.isInstanceOf[FloorTile]}
    }.flatten
  }

  def create(size: Size): Level = {
    // Split the level into grid using BSP
    val root = BspTree(Rectangle(Position(0, 0), size), None)
    while (!areLeavesSmallerThan(maxLeafArea(size), root)) {
      logger.info(s"splitting ${leaves(root).length} nodes, because areas are big: ${leaves(root).map{_.rect.area}.mkString(", ")})}")
      leaves(root) foreach splitLeaf(theme.minRoomSize + 2 * theme.minSplitPadding, theme.splitRatioDeviation)
    }

    // Put a random room in each grid cell
    val rooms = leaves(root).map{t =>
      // +1 -1 hack because when we get exactly minRoomSize, then Random.nextInt would get 0, but it wants a positive N
      val width = theme.minRoomSize + Random.nextInt(t.w - 2 * theme.minSplitPadding - theme.minRoomSize + 1) - 1
      val height = theme.minRoomSize + Random.nextInt(t.h - 2 * theme.minSplitPadding - theme.minRoomSize + 1) - 1
      val x = t.x + theme.minSplitPadding + Random.nextInt(t.w - width)
      val y = t.y + theme.minSplitPadding + Random.nextInt(t.h - height)
      Rectangle(x, y, width, height)
    }

    // Initialize level to be packed with walls
    val level = new Level(size.w, size.h)
    for (x <- 0 until size.w; y <- 0 until size.h) {
      level.tiles(x)(y) = new WallTile(level, x, y)
    }

    // Mark the rooms in the level
    rooms.foreach{r => for (x <- r.x until r.x+r.w; y <- r.y until r.y+r.h) {
      level.tiles(x)(y) = new FloorTile(level, x, y)
    }}

    // Connect rooms
    for (
      depth <- root.depth-1 to 0 by -1;
      tree <- nodesAtDepth(depth, root) if !tree.isLeaf
    ) {
      val (a, b) = (tree.left.get, tree.right.get)
      logger.info(s"level $depth connecting $a and $b")
      tree.splitDir match {
        case Some(SplitDir.Horizontal) =>
          val aEdges = findEdges(level, a.rect, Direction.East)
          val bEdges = findEdges(level, b.rect, Direction.West)
          val commonY = aEdges.map{_.y}.intersect(bEdges.map{_.y}) match {
            case Seq() =>
              logger.info("horizontal misalignment")
              None
            case ys => Some(ys(Random.nextInt(ys.length)))
          }
          commonY.foreach{y =>
            val aDoor = aEdges.find{_.y == y}.get
            val bDoor = bEdges.find{_.y == y}.get
              for (x <- aDoor.x until bDoor.x) {
                level.tiles(x)(y) = new FloorTile(level, x, y)
              }
          }
        case Some(SplitDir.Vertical) =>
          val aEdges = findEdges(level, a.rect, Direction.South)
          val bEdges = findEdges(level, b.rect, Direction.North)
          val commonX = aEdges.map{_.x}.intersect(bEdges.map{_.x}) match {
            case Seq() =>
              logger.info("vertical misalignment")
              None
            case xs => Some(xs(Random.nextInt(xs.length)))
          }
          commonX.foreach{x =>
            val aDoor = aEdges.find{_.x == x}.get
            val bDoor = bEdges.find{_.x == x}.get
            for (y <- aDoor.y until bDoor.y) {
              level.tiles(x)(y) = new FloorTile(level, x, y)
            }
          }
      }

    }
    level
  }
}
