package reversi.hex.coordinates;

import java.util.Objects;

/**
 * An AxialCoordinate is a means of indexing onto a diamond-oriented grid of hexagons. It takes two
 * axis instead of the three of {@link CubicalCoordinate}. Please see
 * <a href="https://www.redblobgames.com/grids/hexagons/">this article</a> explaining the q r s
 * coordinate system for pointy hexagonal grids.
 *
 * @see CubicalCoordinate
 */
public final class AxialCoordinate implements HexPlaneCoord {
  private final int q;
  private final int r;
  private final int s;

  /**
   * Create an AxialCoordinate given the position on the hexagonal grid.
   *
   * @param q the position on the top-left to bottom-right diagonal axis
   * @param r the position on the horizontal axis
   */
  public AxialCoordinate(int q, int r) {
    this.q = q;
    this.r = r;
    this.s = -q - r;
  }

  @Override
  public int getQ() {
    return q;
  }

  @Override
  public int getR() {
    return r;
  }

  @Override
  public int getS() {
    return s;
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
    return "AxialCoordinate{" +
        "q=" + q +
        ", r=" + r +
        ", s=" + s +
        '}';
  }
}
