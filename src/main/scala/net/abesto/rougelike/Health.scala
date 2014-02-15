package net.abesto.rougelike

import net.abesto.rougelike.gui.Color
import scala.collection.immutable.SortedMap


case class Health(value: Integer) {
  def colorCurrent(max: Health, ratioLimitsToColor: SortedMap[Double, Color], best: Color): Color =
  ratioLimitsToColor
    .find{ case ((limit, color)) => value.toDouble / max.value <= limit }.map{_._2}
    .getOrElse(best)
}
