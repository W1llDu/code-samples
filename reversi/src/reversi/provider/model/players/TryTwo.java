package reversi.provider.model.players;

import java.util.Optional;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

/**
 * A class that tries two moves.
 */
public class TryTwo implements InfallibleReversiStrategy {
  ReversiStrategy strategy;
  InfallibleReversiStrategy fallback;

  public TryTwo(ReversiStrategy first, InfallibleReversiStrategy fallback) {
    this.strategy = first;
    this.fallback = fallback;
  }

  public TilePosition chooseMove(ReversiModel model, Player forWhom) {
    Optional<TilePosition> ans = this.strategy.chooseMove(model, forWhom);
    return ans.orElseGet(() -> this.fallback.chooseMove(model, forWhom));
  }
}