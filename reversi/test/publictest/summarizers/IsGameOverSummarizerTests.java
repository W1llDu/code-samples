package publictest.summarizers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.IsGameOverSummarizer;
import reversi.hex.summarizer.reversiinfo.PointsFromMoveAtPointSummarizer;

/**
 * Tests for the {@link IsGameOverSummarizer}.
 */
public class IsGameOverSummarizerTests {
  HexPlane<Player> plane;
  IsGameOverSummarizer summarizer;

  @Before
  public void init() {
    plane = new HexPlaneImpl<>(5);
    summarizer = new IsGameOverSummarizer();
  }

  @Test
  public void testGameOverEmpty() {
    // should return true bc pieces cant be placed next to pieces if there are none
    Assert.assertTrue(summarizer.apply(plane));
  }

  @Test
  public void testGameOverFullBoard() {
    // player 1
    for (HexPlaneCoord coord : plane.getKnownCoords()) {
      plane.setElementAt(coord, Player.PLAYER1);
    }
    Assert.assertTrue(summarizer.apply(plane));

    // player 2
    for (HexPlaneCoord coord : plane.getKnownCoords()) {
      plane.setElementAt(coord, Player.PLAYER2);
    }
    Assert.assertTrue(summarizer.apply(plane));
  }

  @Test
  public void testGameOverPartialBoard() {
    for (HexPlaneCoord coord : plane.getKnownCoords()) {
      if (coord.getR() > 3) {
        plane.setElementAt(coord, Player.PLAYER1);
      } else if (coord.getR() < 3) {
        plane.setElementAt(coord, Player.PLAYER2);
      }
    }
    System.out.println(
            new PointsFromMoveAtPointSummarizer(new AxialCoordinate(-1, 3),
                    Player.PLAYER1).apply(plane));
    Assert.assertTrue(summarizer.apply(plane));
  }
}
