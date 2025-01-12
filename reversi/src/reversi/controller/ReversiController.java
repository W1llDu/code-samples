package reversi.controller;

import reversi.command.UnifiedCommand;
import reversi.exceptions.IllegalCommandException;

/**
 * The controller for Reversi in the MVC architecture. The controller facilitates communication
 * between the View and the Model, allowing for better separation of concerns.
 */
public interface ReversiController {

  /**
   * Hand control over to the controller and start the game.
   */
  void startGame();

  /**
   * Return whether the game is over. A game that has not yet started is not over.
   *
   * @return whether the game is over
   */
  boolean isGameOver();

  /**
   * Run a command on the model and/or the view. This is a side-effecting operation.
   *
   * @param command the command to execute on the model and/or view
   * @param player  the player to run the command as
   * @throws NullPointerException    if the command is null
   * @throws IllegalCommandException if the command cannot be performed
   */
  void accept(UnifiedCommand command, Player player)
      throws NullPointerException, IllegalCommandException;
}
