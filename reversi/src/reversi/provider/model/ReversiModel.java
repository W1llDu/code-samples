package reversi.provider.model;

import reversi.provider.model.board.Board;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.players.Player;

/**
 * Represents the primary model interface for playing a game of Reversi.
 */
public interface ReversiModel extends ReadOnlyReversiModel {

  /**
   * Gets the size of this board (the number of cells in the top row).
   *
   * @param black the player who is playing with black tiles
   * @param white the player who is playing with white tiles
   * @param board the board being used in the game
   * @throws IllegalArgumentException if the size of the board is less than or
   *                                  equal to 1 or if either of the players are invalid
   * @throws IllegalStateException    if the game has already been started
   */
  void startGame(Player black, Player white, Board board);

  /**
   * Creates an identical ReversiModel with the same values.
   *
   * @return a copy of the original model
   */
  ReversiModel cloneModel();

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
   * Plays a piece at the specified row and column if that space is in
   * bounds and unoccupied and the specified color is either
   * {@link TileColor#BLACK} or {@link TileColor#WHITE}.
   *
   * @param row    the row
   * @param col    the column
   * @param player the player making the move
   * @throws IllegalArgumentException if the position is occupied or out of
   *                                  bounds or if the color is
   *                                  {@link TileColor#NONE} or if the move is
   *                                  not a legal move or if it is not the player's turn or
   *                                  if the position is out of bounds
   * @throws IllegalStateException    if the game has not started
   */
  void makeMove(Player player, int row, int col);

  /**
   * Passes the turn for the specified player.
   *
   * @param player the player making the play
   * @throws IllegalArgumentException if it is not the turn of the player
   * @throws IllegalStateException    if the game has not started
   */
  void pass(Player player);

  /**
   * Returns the score for the specified player.
   *
   * @param player the player
   * @return the score for the specified color
   * @throws IllegalStateException if the game has not started
   */
  int getScore(Player player);


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

  @Override
  int getSize();
}