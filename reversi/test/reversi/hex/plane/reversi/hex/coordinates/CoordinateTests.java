package reversi.hex.plane.reversi.hex.coordinates;

import org.junit.Assert;
import org.junit.Test;

import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * Tests for the package-private scope of CoordinateTests.
 */
public class CoordinateTests {

  @Test
  public void testEquality() {
    HexPlaneCoord cubical = new CubicalCoordinate(5, 9, -14);
    HexPlaneCoord axial = new AxialCoordinate(5, 9);
    Assert.assertEquals(cubical, axial);
    Assert.assertEquals(axial, cubical);
  }

  @Test
  public void testInvalidCoord() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new CubicalCoordinate(1, 1, 1));
  }
}
