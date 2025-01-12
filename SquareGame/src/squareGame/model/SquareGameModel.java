package squareGame.model;

import java.util.List;

public interface SquareGameModel {

  /**
   * Makes a move.
   * @param move the move
   * @throws IllegalArgumentException if the row is invalid
   * @throws IllegalStateException if the move is invalid
   */
  void move(Move move) throws IllegalArgumentException, IllegalStateException;

  /**
   * Gets rows.
   */
  List<Integer> getRows();

  /**
   * Checks if the game is over.
   */
  boolean isGameOver();

  /**
   * Checks if the move is valid.
   * @param move the move
   */
  boolean isMoveValid(Move move);
}
