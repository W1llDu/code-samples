package reversi.provider.model.players;

import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;


/**
 * The ReversiPlayer class represents a player in the Reversi game.
 * It implements the Player interface and provides methods to access the player's tile color.
 */
public class ReversiPlayer implements Player {
  private final TileColor color;

  /**
   * Constructs a ReversiPlayer with the specified tile color.
   *
   * @param color the tile color of the player (either TileColor.WHITE or TileColor.BLACK)
   */
  public ReversiPlayer(TileColor color) {
    this.color = color;
  }

  @Override
  public TileColor getColor() {
    return color;
  }

  @Override
  public Boolean isHuman() {
    return true;
  }

  @Override
  public TilePosition makeMove() {
    throw new IllegalStateException();
  }
}
