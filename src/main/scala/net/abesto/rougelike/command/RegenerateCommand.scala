package net.abesto.rougelike.command

import net.abesto.rougelike.{Tile, Creature}
import net.abesto.rougelike.levelfactory.tyrant.TyrantLikeLevelFactory
import net.abesto.rougelike.levelfactory.{Size, LevelFactory}

class RegenerateCommand(factory: LevelFactory, size: Size) extends Command {
  override def apply(c: Creature): Seq[Tile] = factory.create(size).tiles.flatten.toSeq
}
