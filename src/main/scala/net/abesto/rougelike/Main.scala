package net.abesto.rougelike

import com.googlecode.lanterna.terminal.swing.SwingTerminal
import net.abesto.rougelike.gui.Screen
import net.abesto.rougelike.levelfactory.WalledEmptyLevelFactory
import net.abesto.rougelike.command.{RegenerateCommand, PassCommand, MoveCommand}
import net.abesto.rougelike.levelfactory.tyrant.TyrantLikeLevelFactory


object Main extends App {
  val term = new SwingTerminal
  term.enterPrivateMode()
  val screen = new Screen(term)

  val level = TyrantLikeLevelFactory.create(160, 38)
  val player = new Player(level.tiles(80)(16))
  var turn = 0

  screen.writeHealth(Health(90), Health(100))
  screen.writeTurn(turn)
  screen.drawLevel(level)

  var input = term.readInput()
  while (input == null || input.getCharacter != 'q') {
    if (input != null) {
      val command = input.getCharacter match {
        case 'k' => MoveCommand.north
        case 'h' => MoveCommand.west
        case 'j' => MoveCommand.south
        case 'l' => MoveCommand.east
        case 'r' => new RegenerateCommand(160, 38)
        case  _  => PassCommand
      }
      command(player) foreach screen.drawTile
      turn += 1
      screen.writeTurn(turn)
    }
    Thread.sleep(1)
    input = term.readInput()
  }

  term.exitPrivateMode()

}
