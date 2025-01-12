package reversi.hex.coordinates;

/**
 * A HexPlaneCoord represents a position on a hexagonal grid. All implementations of this interface
 * that represent equivalent coordinates must share the same hashcode. Implementations ought to
 * override {@link Object#toString()}.
 */
public interface HexPlaneCoord {

  /**
   * Get the {@code q} position of this coordinate.
   *
   * @return the position relative to the top-left to bottom-right axis
   */
  int getQ();

  /**
   * Get the {@code r} position of this coordinate.
   *
   * @return the position relative to the horizontal axis
   */
  int getR();

  /**
   * Get the {@code s} position of this coordinate.
   *
   * @return the position relative to the bottom-left to top-right
   */
  int getS();
}
