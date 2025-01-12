package reversi.provider.model.board;

/**
 * Represents a pair of coordinates (row, column) on a game board.
 * Instances of this class are immutable, once created, the x and y values cannot be changed.
 */
public class TilePosition {
  private final int row;
  private final int col;

  /**
   * Constructs a new TilePositionXY object with the specified row and column values.
   *
   * @param row the row value
   * @param col the column value
   */
  public TilePosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Returns the row value of this TilePositionXY.
   *
   * @return the row value
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column value of this TilePositionXY.
   *
   * @return the column value
   */
  public int getCol() {
    return col;
  }

  /**
   * Returns a string value of a TilePosition and its coordinates in row, col format.
   *
   * @return the toString implementation
   */
  @Override
  public String toString() {
    return String.format("(%d, %d)", row, col);
  }

  /**
   * Returns a boolean value indicating if the object other has the same properties as this.
   *
   * @return a boolean value if the objects are equal
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof TilePosition)) {
      return false;
    }
    TilePosition otherTP = (TilePosition) other;
    return this.getCol() == otherTP.getCol() && this.getRow() == otherTP.getRow();
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
