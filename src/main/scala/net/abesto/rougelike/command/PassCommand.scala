package net.abesto.rougelike.command

import net.abesto.rougelike.{Tile, Creature}

object PassCommand extends Command {
  override def apply(c: Creature): Seq[Tile] = Seq()
}
