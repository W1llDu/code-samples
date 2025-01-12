package reversi.provider.model.players;

import java.util.Optional;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

/**
 * Method Object Interface representing a strategy for the Reversi Game.
 */
public interface ReversiStrategy {

  /**
   * Selects the next move to be made by the player according to the implemented strategy.
   *
   * @return The {@code TilePosition} representing the chosen move or empty if no move can
   *                               be determined by the strategy.
   * @throws IllegalStateException If the strategy cannot determine a valid move.
   *                               This may occur if the game board is in an unexpected state.
   */
  public Optional<TilePosition> chooseMove(ReversiModel model, Player forWhom);
}
