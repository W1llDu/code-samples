package publictest;

import org.junit.Assert;
import org.junit.Test;

import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.provider.adapters.hex.ProviderHexCoordAdapter;

public class ProviderHexCoordTests {

  @Test
  public void testCoords() {
    compare(new AxialCoordinate(0, -2),
        new ProviderHexCoordAdapter(0, 0, 3));
    compare(new AxialCoordinate(0, 0),
        new ProviderHexCoordAdapter(2, 2, 3));
    compare(new AxialCoordinate(0, 2),
        new ProviderHexCoordAdapter(2, 4, 3));
    compare(new AxialCoordinate(0, 1),
        new ProviderHexCoordAdapter(2, 3, 3));

    compare(new AxialCoordinate(0, -3),
        new ProviderHexCoordAdapter(0, 0, 4));
    compare(new AxialCoordinate(3, -3),
        new ProviderHexCoordAdapter(3, 0, 4));
    compare(new AxialCoordinate(3, -3),
        new ProviderHexCoordAdapter(3, 0, 4));
    compare(new AxialCoordinate(3, 0),
        new ProviderHexCoordAdapter(6, 3, 4));
    compare(new AxialCoordinate(1, 1),
        new ProviderHexCoordAdapter(4, 4, 4));
  }

  private void compare (HexPlaneCoord a, HexPlaneCoord b) {
    Assert.assertEquals(a, b);
  }
}
