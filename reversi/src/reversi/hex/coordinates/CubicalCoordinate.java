package reversi.hex.coordinates;

import java.util.Objects;

/**
 * An CubicalCoordinate is a means of indexing onto a diamond-oriented grid of hexagons. It takes
 * three axis instead of the two of {@link AxialCoordinate}. Please see
 * <a href="https://www.redblobgames.com/grids/hexagons/">this article</a> explaining the q r s
 * coordinate system for pointy hexagonal grids.
 *
 * @see AxialCoordinate
 */
public final class CubicalCoordinate implements HexPlaneCoord {
  private final int q;
  private final int r;
  private final int s;

  /**
   * Create a CubicalCoordinate given the position on the hexagonal grid.
   *
   * @param q the position on the top-left to bottom-right diagonal axis
   * @param r the position on the horizontal axis
   * @param s the position on the axis between q and r
   */
  public CubicalCoordinate(int q, int r, int s) {
    if (q + r + s != 0) {
      throw new IllegalArgumentException("Invalid coordinates for a cubical coordinate: " +
          "{ q:" + q + " r: " + r + " s: " + s + " }");
    }
    this.q = q;
    this.r = r;
    this.s = s;
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
    return "CubicalCoordinate{" +
        "q=" + q +
        ", r=" + r +
        ", s=" + s +
        '}';
  }
}