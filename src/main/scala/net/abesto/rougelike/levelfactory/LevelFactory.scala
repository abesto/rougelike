package net.abesto.rougelike.levelfactory

import net.abesto.rougelike.Level

trait LevelFactory {
  def create(size: Size): Level
}
