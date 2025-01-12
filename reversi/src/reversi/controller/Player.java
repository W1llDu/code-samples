package reversi.controller;

/**
 * A Player is an enumeration of the players of the game. In the case of Reversi, there are up to
 * two players.
 */
public enum Player {
  PLAYER1,
  PLAYER2;

  /**
   * Get the other player. That is, if used on PLAYER1, PLAYER2 is returned and vice versa.
   *
   * @return the opposing player
   */
  public Player getOther() {
    if (this == Player.PLAYER1) {
      return PLAYER2;
    }
    return PLAYER1;
  }

  @Override
  public String toString() {
    if (this == Player.PLAYER1) {
      return "X";
    }
    return "O";
  }
}
