package net.abesto.rougelike.command

import net.abesto.rougelike.{Tile, Creature}

trait Command {
  def apply(c: Creature): Seq[Tile]
}
