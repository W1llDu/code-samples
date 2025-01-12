package reversi.provider.model.players;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

/**
 * Method Object Interface representing an infallible strategy for the Reversi Game.
 */
public interface InfallibleReversiStrategy {

  /**
   * Selects the next move to be made by the player according to the implemented strategy.
   *
   * @return The {@code TilePosition} representing the chosen move.
   * @throws IllegalStateException If the strategy cannot determine a valid move.
   *                               This may occur if the game board is in an unexpected state.
   */
  public TilePosition chooseMove(ReversiModel model, Player forWhom) throws IllegalStateException;
}
