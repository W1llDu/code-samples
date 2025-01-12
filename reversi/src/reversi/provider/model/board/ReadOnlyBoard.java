package reversi.provider.model.board;

import java.util.List;

/**
 * Interface representing a Reversi game board that you can only read from.
 */
public interface ReadOnlyBoard {

  /**
   * Gets the tile at the specified location. Where (0, 0) is the
   * top left of the board.
   * {@link TileColor#BLACK} or {@link TileColor#WHITE}.
   *
   * @param row the row
   * @param col the column
   * @return the tile at the given position
   * @throws IllegalArgumentException if the tile is out of bounds
   */
  Tile getTileAt(int row, int col);

  /**
   * Gets the size of this board (the number of cells in the top row).
   *
   * @return the radius of the board including the center
   */
  int getSize();

  /**
   * Gets the List of Tiles as a board.
   *
   * @return the board list.
   */
  List<List<Tile>> getBoard();
}
