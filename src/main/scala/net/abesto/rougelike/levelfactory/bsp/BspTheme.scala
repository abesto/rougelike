package net.abesto.rougelike.levelfactory.bsp

case class BspTheme(splitRatioDeviation: Float,
                    minRoomSize: Int,
                    minSplitPadding: Int
                     )

object BspTheme {
  val standard = BspTheme(.05f, 10, 1)
}
