package reversi.model;

import org.junit.Assert;
import org.junit.Test;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.PointsFromMoveAtPointSummarizer;
import reversi.view.ReversiTextView;
import reversi.view.ReversiView;

/**
 * Tests for an in-progress view using the package private constructor for the model.
 */
public class InProgressViewRenderTests {

  @Test
  public void testRenderRadius5() {
    HexPlane<Player> plane = new HexPlaneImpl<>(5);
    Appendable out = new StringBuilder();

    for (int q = -2; q < 2; q++) {
      for (int r = -2; r < 2; r++) {
        if (r > 0 && q < 0) {
          plane.setElementAt(new AxialCoordinate(q, r), Player.PLAYER1);
        } else {
          plane.setElementAt(new AxialCoordinate(q, r), Player.PLAYER2);
        }
      }
    }
    MutableReversiModel model = new ReversiModel(plane, Player.PLAYER1);
    ReversiView view = new ReversiTextView(out, model);

    view.render();
    String expected =
            "    _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ \n" +
                    "  O O O O _ _ _ \n" +
                    " _ O O O O _ _ _ \n" +
                    "_ _ O O O O _ _ _ \n" +
                    " _ _ X X O O _ _ \n" +
                    "  _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _\n";
    Assert.assertEquals(expected, out.toString());
    // check the score from the move
    int score = new PointsFromMoveAtPointSummarizer(
        new AxialCoordinate(-1, -3), Player.PLAYER1)
        .apply(model.getHexPlane());
    Assert.assertEquals(4, score);
  }
}
