package publictest.summarizers;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.CoordsToFlipGivenMoveSummarizer;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;

/**
 * Tests for {@link CoordsToFlipGivenMoveSummarizer}.
 */
public class CoordsToFlipGivenMoveSummarizerTests {

  @Test
  public void testNoPoints() {
    Assert.assertEquals(List.of(new CubicalCoordinate(0, 0, 0)),
            new CoordsToFlipGivenMoveSummarizer(new AxialCoordinate(0, 0),
                    Player.PLAYER2)
                    .apply(new HexPlaneImpl<>(1)));
  }

  @Test
  public void testFirstMove() {
    MutableReversiModel model = new ReversiModel(4);
    List<HexPlaneCoord> summary = new CoordsToFlipGivenMoveSummarizer(
        new AxialCoordinate(1, -2), Player.PLAYER1)
        .apply(model.getHexPlane());
    Assert.assertTrue(summary.contains(new CubicalCoordinate(1, -2, 1)));
    Assert.assertTrue(summary.contains(new CubicalCoordinate(1, -1, 0)));
  }

  @Test
  public void testInvertToEdge() {
    HexPlane<Player> four = new HexPlaneImpl<>(4);
    four.setElementAt(new AxialCoordinate(0, 1), Player.PLAYER2);
    four.setElementAt(new AxialCoordinate(0, 2), Player.PLAYER2);
    four.setElementAt(new AxialCoordinate(0, 3), Player.PLAYER2);
    four.setElementAt(new AxialCoordinate(0, -1), Player.PLAYER2);
    four.setElementAt(new AxialCoordinate(0, -2), Player.PLAYER2);
    four.setElementAt(new AxialCoordinate(0, -3), Player.PLAYER1);
    List<HexPlaneCoord> summary = new CoordsToFlipGivenMoveSummarizer(
            new AxialCoordinate(0, 0),
            Player.PLAYER1).apply(four);
    Assert.assertEquals(3, summary.size());
    Assert.assertTrue(summary.contains(new AxialCoordinate(0, 0)));
    Assert.assertTrue(summary.contains(new AxialCoordinate(0, -1)));
    Assert.assertTrue(summary.contains(new AxialCoordinate(0, -2)));
  }
}
