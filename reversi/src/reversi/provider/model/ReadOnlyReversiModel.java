package reversi.provider.model;


import reversi.provider.model.board.Board;
import reversi.provider.model.players.Player;

/**
 * Represents a model interface for playing a game of Reversi with read only methods.
 */
public interface ReadOnlyReversiModel {

  /**
   * Gets the player whose turn it is to make a move.
   *
   * @return the player or null if it is no player's turn
   * @throws IllegalStateException if the game has not started
   */
  Player getTurn();

  /**
   * Checks whether a play can be made at the specified row and column,
   * where (0, 0) is the top left cell of the board.
   *
   * @param row    the row
   * @param col    the column
   * @param player the player placing the tile
   * @return true if the position is empty and the move is allowable
   * @throws IllegalStateException if the game has not started
   */
  boolean canPlay(Player player, int row, int col);

  /**
   * Returns the score for the specified player.
   *
   * @param player the player
   * @return the score for the specified color
   * @throws IllegalStateException if the game has not started
   */
  int getScore(Player player);

  /**
   * Gets the board of the current game.
   *
   * @return the board
   * @throws IllegalStateException if the game has not started
   */
  Board getBoard();

  /**
   * Returns the potential score increase for a move. Will be useful for the AI.
   *
   * @param player the player
   * @param row    the row
   * @param col    the column
   * @return the potential score increase for the specified move
   * @throws IllegalArgumentException if the move is not allowed
   * @throws IllegalStateException    if the game has not started
   */
  int getPotentialScore(Player player, int row, int col);

  /**
   * Checks whether the game is over. There are three ways the game can end:
   * <ol>
   *     <li>
   *         Each player passes one after another.
   *     </li>
   *     <li>
   *         There are no legal moves left for either player.
   *     </li>
   *     <li>
   *         All of the cells are occupied.
   *     </li>
   * </ol>
   *
   * @return true if the game is over, false otherwise
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  int getSize();
}