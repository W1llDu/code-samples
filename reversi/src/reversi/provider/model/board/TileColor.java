package reversi.provider.model.board;

import java.awt.Color;

/**
 * A representation of the colors of the tiles in a Reversi game.
 */
public enum TileColor {
  /**
   * An empty position.
   */
  NONE("none", Color.GRAY),

  /**
   * A white tile or a position with a white tile.
   */
  WHITE("White", Color.WHITE),

  /**
   * A black tile or a position with a black tile.
   */
  BLACK("Black", Color.BLACK);

  private final String name;

  TileColor(String name, Color color) {
    this.name = name;
  }

  /**
   * Gets the capitalized name of this tile color.
   *
   * @return the capitalized name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the opposite of this color, where {@link #BLACK} and
   * {@link #WHITE} are opposites.
   *
   * @return the opposite of this color
   * @throws IllegalArgumentException if this is {@link #NONE}
   */
  public TileColor getOpposite() {
    switch (this) {
      case WHITE:
        return BLACK;
      case BLACK:
        return WHITE;
      case NONE:
        return NONE;
      default:
        throw new IllegalArgumentException("Cannot find this.TileColor");
    }
  }
}
