package reversi.model;

import reversi.controller.Player;
import reversi.exceptions.MoveOutOfBoundsException;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * The all-powerful version of {@link ReadOnlyReversiModel}, enabling mutable operations on the
 * underlying model. The model must take care of the internal state of the game, such as the current
 * board layout and the current player. Note that even though it keeps track of the current player,
 * moves involving a player will still require the player to be passed as an argument. This is for
 * broader compatibility with the various ways to use a {@code MutableReversiModel}, such as with an
 * asynchronous Controller.
 */
public interface MutableReversiModel extends ReadOnlyReversiModel<Player> {
  /**
   * Make a move at a specific location given the player attempting to move there. Useful for
   * asynchronous Controllers.
   *
   * @param coordinate the position the move is being made at
   * @param player     the player making the move
   * @throws MoveOutOfBoundsException if the move is invalid because it is out of bounds
   * @throws IllegalStateException    if it is not the supplied player's move or if the location is
   *                                  already inhabited
   */
  void makeMoveAsPlayer(HexPlaneCoord coordinate, Player player)
      throws MoveOutOfBoundsException, IllegalStateException;

  /**
   * Allows a player to pass their move.
   *
   * @param player the player passing their move
   * @throws IllegalStateException if it is not the supplied player's move.
   */
  void passAsPlayer(Player player)
      throws IllegalStateException;

  /**
   * Get the player whose turn it is.
   *
   * @return the player who is allowed to make a move next
   */
  @Override
  Player getPlayer();
}
