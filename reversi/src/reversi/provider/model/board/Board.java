package reversi.provider.model.board;

import java.util.List;

/**
 * Interface representing a Reversi game board.
 */
public interface Board {

  /**
   * Plays a piece at the specified row and column if that space is in
   * bounds and unoccupied and the specified color is either
   * {@link TileColor#BLACK} or {@link TileColor#WHITE}. Where the top left of the
   * board is the origin (0,0), the tile to the right of it is (0,1) and the one to the
   * bottom left of it is (1,0).
   *
   * @param row   the row
   * @param col   the column
   * @param color the player making the move
   * @throws IllegalArgumentException if the position is occupied or out of
   *                                  bounds or if the color is
   *                                  {@link TileColor#NONE} or if the move is
   *                                  not a legal move
   */
  void updateBoard(TileColor color, int row, int col);


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
