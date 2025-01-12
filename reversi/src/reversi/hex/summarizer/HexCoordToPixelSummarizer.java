package reversi.hex.summarizer;

import java.awt.geom.Point2D;

import reversi.hex.plane.HexPlane;

/**
 * Summarize a plane by converting the hexagonal coordinate plane into a 2D cartesian plane. The
 * value in the key of the HexPlane represents the corresponding 2D coordinate the center of the Hex
 * belongs according to its radius, which is supplied in the constructor for this class.
 */
public class HexCoordToPixelSummarizer<T>
    implements HexPlaneSummarizer<T, HexPlane<Point2D>> {
  private final int radius;

  /**
   * Given the radius of each hex cell, construct a new {@link HexCoordToPixelSummarizer}.
   *
   * @param radius the radius of each cell
   * @throws IllegalArgumentException if the radius is not positive
   */
  public HexCoordToPixelSummarizer(int radius) {
    if (radius < 1) {
      throw new IllegalArgumentException("Cannot create HexCoordToPixelSummarizer with " +
          "non-positive radius");
    }
    this.radius = radius;
  }

  @Override
  public HexPlane<Point2D> apply(HexPlane<T> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return plane.keysetMap(coord -> {
      double x = radius * (Math.sqrt(3) * coord.getQ() + Math.sqrt(3) / 2d * coord.getR());
      double y = radius * (3d / 2d * coord.getR());
      return new Point2D.Double(x, y);
    });
  }
}
