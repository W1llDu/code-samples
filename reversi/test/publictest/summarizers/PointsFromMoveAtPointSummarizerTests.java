package publictest.summarizers;

import org.junit.Assert;
import org.junit.Test;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.PointsFromMoveAtPointSummarizer;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;

/**
 * Tests for {@link PointsFromMoveAtPointSummarizer}.
 */
public class PointsFromMoveAtPointSummarizerTests {
  @Test
  public void testNoPoints() {
    Assert.assertEquals(Integer.valueOf(1),
            new PointsFromMoveAtPointSummarizer(new AxialCoordinate(0, 0),
                    Player.PLAYER1).apply(new HexPlaneImpl<>(1)));
  }

  @Test
  public void testFirstMove() {
    MutableReversiModel model = new ReversiModel(3);
    int summary = new PointsFromMoveAtPointSummarizer(
        new AxialCoordinate(1, -2), Player.PLAYER1)
        .apply(model.getHexPlane());
    Assert.assertEquals(2, summary);
  }
}
