package reversi.provider.model.players;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

import java.util.Optional;

/**
 * A Reversi strategy that aims to capture as many pieces as possible and falls back to another
 * strategy if no suitable move is found. Implements the {@code ReversiStrategy} interface.
 */
public class CaptureMax implements ReversiStrategy {

  /**
   * Chooses a move based on capturing as many pieces as possible.
   *
   * @return an Optional containing the TilePosition representing the chosen move, or an
   *                            empty Optional if no suitable move is found.
   */
  @Override
  public Optional<TilePosition> chooseMove(ReversiModel model, Player player) {
    int maxPotentialScore = Integer.MIN_VALUE;
    TilePosition bestMove = new TilePosition(-100, -100);
    MoveGenerator mg = new MoveGenerator();
    for (TilePosition pos : mg.generateMoves(model, player)) {
      int potentialScore = model.getPotentialScore(player, pos.getRow(), pos.getCol());
      if (potentialScore > maxPotentialScore) {
        maxPotentialScore = potentialScore;
        bestMove = pos;
      }
      if (potentialScore == maxPotentialScore && isMoreUpLeft(pos, bestMove)) {
        bestMove = pos;
      }
    }
    if (bestMove.getRow() == -100) {
      return Optional.empty();
    }
    return Optional.of(bestMove);
  }

  private boolean isMoreUpLeft(TilePosition thisMoreUL, TilePosition thanThis) {
    return (thisMoreUL.getRow() + thisMoreUL.getCol()) < (thanThis.getRow() + thanThis.getCol());
  }

}
