package reversi.provider.model.players;

import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;

/**
 * Interface representing a player in the Reversi game.
 */
public interface Player {
  /**
   * Gets the color of tiles that the player is playing with.
   *
   * @return the color
   */
  TileColor getColor();

  /**
   * Returns if this player is human or controlled by an AI strategy.
   *
   * @return the boolean value of whether it is human
   */
  Boolean isHuman();

  TilePosition makeMove();
}
