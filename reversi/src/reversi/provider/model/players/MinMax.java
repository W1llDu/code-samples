package reversi.provider.model.players;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.Tile;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;

/**
 * A Reversi strategy based on the Minimax algorithm with alpha-beta pruning.
 * Implements the {@code InfallibleReversiStrategy} interface.
 */
public class MinMax implements InfallibleReversiStrategy {
  private final int depth = 3;

  /**
   * Chooses the best move using the Minimax algorithm with alpha-beta pruning.
   *
   * @return the TilePosition representing the chosen move.
   * @throws IllegalStateException if invoked when the game is not in a valid state.
   */
  @Override
  public TilePosition chooseMove(ReversiModel model, Player player) throws IllegalStateException {
    int bestValue = 0;
    TilePosition bestMove = new TilePosition(-1, -1);
    MoveGenerator mg = new MoveGenerator();
    for (TilePosition pos : mg.generateMoves(model, player)) {
      ReversiModel clonedModel = model.cloneModel();
      int posVal = minMax(
              pos,
              false,
              clonedModel,
              Integer.MIN_VALUE,
              Integer.MAX_VALUE,
              player,
              depth);
      if (posVal > bestValue) {
        bestValue = posVal;
        bestMove = pos;
      }
    }
    return bestMove;
  }

  private int minMax(TilePosition move, boolean maximizingPlayer,
                     ReversiModel rmodel, int alpha, int beta, Player player, int depth) {
    ReversiModel newGameState = rmodel.cloneModel();
    if (newGameState.isGameOver() || depth == 0) {
      return evaluateBoard(newGameState, player);
    }
    if (isPass(move)) {
      newGameState.pass(newGameState.getTurn());
    } else {
      newGameState.makeMove(newGameState.getTurn(), move.getRow(), move.getCol());
    }
    int value;
    if (maximizingPlayer) {
      value = Integer.MIN_VALUE;
      MoveGenerator mg = new MoveGenerator();
      for (TilePosition pos : mg.generateMoves(newGameState, newGameState.getTurn())) {
        value = Math.max(
                value,
                minMax(pos,
                        false,
                        newGameState,
                        alpha, beta, player, depth - 1));
        alpha = Math.max(alpha, value);
        if (beta <= alpha) {
          break;
        }
      }
    } else {
      value = Integer.MAX_VALUE;
      MoveGenerator mg = new MoveGenerator();
      for (TilePosition pos : mg.generateMoves(newGameState, newGameState.getTurn())) {
        value = Math.min(value,
                minMax(pos,
                        true,
                        newGameState,
                        alpha, beta, player, depth - 1));
        beta = Math.min(beta, value);
        if (beta <= alpha) {
          break;
        }
      }
    }
    return value;
  }


  private int evaluateBoard(ReversiModel model, Player player) {
    int blackTiles = 0;
    int whiteTiles = 0;
    for (int row = 0; row < model.getBoard().getBoard().size(); row++) {
      for (int col = 0; col < model.getBoard().getBoard().get(row).size(); col++) {
        Tile t = model.getBoard().getTileAt(row, col);
        if (t.getColor().equals(TileColor.WHITE)) {
          whiteTiles++;
        } else if (t.getColor().equals(TileColor.BLACK)) {
          blackTiles++;
        }
      }
    }
    if (model.isGameOver()) {
      int winner = 0;
      if (blackTiles > whiteTiles) {
        winner = player.getColor().equals(TileColor.BLACK) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      } else if (whiteTiles > blackTiles) {
        winner = player.getColor().equals(TileColor.WHITE) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      }
      return winner;
    } else {
      return player.getColor().equals(
              TileColor.WHITE) ? whiteTiles - blackTiles : blackTiles - whiteTiles;
    }
  }


  private boolean isPass(TilePosition move) {
    return move.getRow() == -1 && move.getCol() == -1;
  }


}
