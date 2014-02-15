package net.abesto.rougelike.command

import net.abesto.rougelike.{Tile, Creature}
import net.abesto.rougelike.levelfactory.tyrant.TyrantLikeLevelFactory

class RegenerateCommand(w: Int, h: Int) extends Command {
  override def apply(c: Creature): Seq[Tile] = TyrantLikeLevelFactory.create(w, h).tiles.flatten.toSeq
}
