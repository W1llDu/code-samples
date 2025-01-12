package reversi.hex.plane;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.CubicalCoordinate;

/**
 * Test class for HexPlane.
 */
public class HexPlaneTests {

  @Test
  public void testConstructorIllegalThrowsException() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new HexPlaneImpl<Boolean>(0));
  }

  @Test
  public void testConstructorRadiusOne() {
    HexPlane<Boolean> zero = new HexPlaneImpl<>(1);
    Assert.assertEquals(1, zero.getPlane().size());
    Assert.assertEquals(Optional.empty(), zero.getAtHex(new AxialCoordinate(0, 0)));
    Assert.assertEquals(Optional.empty(),
            zero.getAtHex(new CubicalCoordinate(0, 0, 0)));
  }

  @Test
  public void testConstructorRadiusFour() {
    HexPlane<Boolean> three = new HexPlaneImpl<>(4);
    Assert.assertEquals(37, three.getPlane().size());
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(0, -3)));
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(3, -3)));
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(3, 0)));
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(0, 3)));
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(-3, 3)));
    Assert.assertEquals(Optional.empty(), three.getAtHex(new AxialCoordinate(-3, 0)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(4, -4)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(-3, 4)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(4, 0)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(-4, 1)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(0, 4)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> three.getAtHex(new AxialCoordinate(-1, -3)));
  }

  @Test
  public void testCoordinatesAreIsomorphic() {
    HexPlane<Integer> three = new HexPlaneImpl<>(4);
    three.setElementAt(new AxialCoordinate(0, 2), 6);
    Assert.assertEquals(Optional.of(6),
            three.getAtHex(new CubicalCoordinate(0, 2, -2)));
    three.setElementAt(new CubicalCoordinate(-1, -2, 3), 9);
    Assert.assertEquals(Optional.of(9),
            three.getAtHex(new AxialCoordinate(-1, -2)));
  }
}
