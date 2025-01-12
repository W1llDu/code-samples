package reversi.provider.model.board;

import java.util.List;

/**
 * The Tile interface represents a tile on the Reversi game board.
 * It provides methods to set and get neighboring tiles, as well as methods
 * to get and set the color of the tile.
 */
public interface Tile {
  /**
   * Sets the neighboring tile based on the specified position.
   *
   * @param position The position relative to this tile (e.g., "l", "ul", "br").
   * @param tile     The tile to be set as the neighbor at the specified position.
   * @throws IllegalArgumentException If the position is invalid or cannot be resolved.
   */
  void setNeighbor(String position, Tile tile);

  /**
   * Gets the color of this tile.
   *
   * @return The color of this tile.
   */
  TileColor getColor();

  /**
   * Gets a list of all the neighbors of this tile.
   *
   * @return The color of this tile.
   */
  List<Tile> getNeighbors();

  /**
   * Sets the color of this tile.
   */
  void setColor(TileColor color);
}
