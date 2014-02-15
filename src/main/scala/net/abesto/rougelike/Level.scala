package net.abesto.rougelike

case class Level(w: Int, h: Int) {
  val tiles = Array.ofDim[Tile](w, h)
}
