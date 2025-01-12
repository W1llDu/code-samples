package reversi.provider.model.players;

import java.util.ArrayList;
import java.util.List;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

/**
 * The {@code MoveGenerator} class is responsible for generating a list of valid moves
 * for the current state of a Reversi game.
 */
public class MoveGenerator {

  /**
   * Generates a list of valid moves for the current state of the Reversi game.
   *
   * @param model the Reversi model representing the current game state.
   * @return a list of TilePositions representing valid moves, including passing.
   * @throws NullPointerException if the provided model is {@code null}.
   */
  public List<TilePosition> generateMoves(ReversiModel model, Player player) {
    List<TilePosition> possibleMoves = new ArrayList<>();
    for (int row = 0; row < model.getBoard().getBoard().size(); row++) {
      for (int col = 0; col < model.getBoard().getBoard().get(row).size(); col++) {
        if (model.canPlay(player, row, col)) {
          possibleMoves.add(new TilePosition(row, col));
        }
      }
    }
    // Include passing as a move
    possibleMoves.add(new TilePosition(-1, -1));
    return possibleMoves;
  }
}
