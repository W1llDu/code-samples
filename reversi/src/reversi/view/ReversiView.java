package reversi.view;

import reversi.command.ViewCommand;
import reversi.controller.ControllerCallback;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * A ReversiView is the view of the MVC architecture for Reversi. ReversiViews implement the Command
 * Pattern for dynamic updates of the interface. Views needn't provide functionality for every
 * method (i.e., no-ops are acceptable). Methods that ought never be no-ops are documented such.
 */
public interface ReversiView {

  /**
   * Display the view somehow. Examples include text-only TUIs and CLIs or graphical GUIs.
   * Additionally, this should result in a repaint if such an operation is supported.
   *
   * @return true iff the view requires focus be returned
   */
  boolean render();

  /**
   * Set the supplied callback to the controller. Communication between the view and the controller
   * should go through this channel. If the callback is null, the view will be unable to communicate
   * with the controller.
   *
   * @param controllerCallback the callback to the controller
   */
  void setControllerCallback(ControllerCallback controllerCallback);

  /**
   * Show an error message to the player. Ideally, these messages differentiate themselves from
   * ordinary messages so the player knows it's an error.
   *
   * @param message the message to show the player
   * @throws NullPointerException if the message is null
   */
  void showErrorMessage(String message) throws NullPointerException;

  /**
   * Highlight a specific tile to draw attention to it for a player.
   *
   * @param coord the position to highlight
   * @throws IndexOutOfBoundsException if the coord is out of bounds
   * @throws NullPointerException      if coord is null
   */
  void highlightTile(HexPlaneCoord coord) throws IndexOutOfBoundsException, NullPointerException;

  void clearHighlights();

  /**
   * Perform a {@link ViewCommand} on the view.
   *
   * @param command the command to perform
   * @throws NullPointerException if command is null
   */
  void accept(ViewCommand command) throws NullPointerException;

  /**
   * Show a helpful game over message. This is similar to {@link #showErrorMessage(String)}, but
   * specifically shows a game over message. Calls to this method should not have side effects
   * beyond displaying the message.
   */
  void gameEnd();
}
