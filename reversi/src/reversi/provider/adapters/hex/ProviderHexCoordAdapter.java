package reversi.provider.adapters.hex;

import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.summarizer.BoardSizeSummarizer;
import reversi.model.ReadOnlyReversiModel;
import reversi.model.ReversiModel;

/**
 * Adapter for {@link HexPlaneCoord} for the provider's row-col coordinate system.
 */
public class ProviderHexCoordAdapter implements HexPlaneCoord {
  private final int col;
  private final int row;
  private final int size;

  /**
   * Create a {@link HexPlaneCoord} given the row and column and radius of the board. The radius
   * is the number of hexes on the outermost parts of the board. Topmost leftmost position is
   * (0,0).
   *
   * @param col the column
   * @param row the row
   * @param radius the radius of the board
   */
  public ProviderHexCoordAdapter(int row, int col, int radius) {
    this.col = col;
    this.row = row;
    this.size = radius;
  }

  /**
   * Create a new HexCoordAdapter given the column and row to make a move. This is a convenience
   * constructor, and behaves the same as {@link #ProviderHexCoordAdapter(int, int, int)},
   * except the radius is derived from the model.
   *
   * @param col the column
   * @param row the row
   * @param model the model to use to get the radius
   */
  public <T> ProviderHexCoordAdapter(int row, int col, ReadOnlyReversiModel<T> model) {
    this.row = row;
    this.col = col;
    this.size = new BoardSizeSummarizer<T>().apply(model.getHexPlane());
  }

  @Override
  public int getQ() {
    return -getR()-getS();
  }

  @Override
  public int getR() {
    return row-size;
  }

  @Override
  public int getS() {
    if (row > size) {
      return size-col-row+size;
    } else {
      return size-col;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HexPlaneCoord)) {
      return false;
    }
    HexPlaneCoord that = (HexPlaneCoord) o;
    return getQ() == that.getQ() && getR() == that.getR() && getS() == that.getS();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getQ(), getR(), getS());
  }

  @Override
  public String toString() {
    return "ProviderHexCoord{" +
        "q=" + getQ() +
        ", r=" + getR() +
        ", s=" + getS() +
        '}';
  }
}
