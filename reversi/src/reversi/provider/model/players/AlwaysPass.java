package reversi.provider.model.players;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

/**
 * A fallback strategy that always chooses to pass.
 */
public class AlwaysPass implements InfallibleReversiStrategy {

  /**
   * Chooses to pass as a fallback move.
   *
   * @return a TilePosition representing a pass move.
   * @throws IllegalStateException if the move is not allowed.
   */
  @Override
  public TilePosition chooseMove(ReversiModel model, Player player) throws IllegalStateException {
    if (model.isGameOver()) {
      throw new IllegalStateException("Game is over");
    }
    return new TilePosition(-1, -1);
  }
}
