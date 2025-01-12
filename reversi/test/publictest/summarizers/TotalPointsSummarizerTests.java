package publictest.summarizers;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;

/**
 * Tests for {@link TotalPointsSummarizer}.
 */
public class TotalPointsSummarizerTests {

  @Test
  public void testEmpty() {
    HexPlane<Player> four = new HexPlaneImpl<>(4);
    Assert.assertEquals(Integer.valueOf(0),
            new TotalPointsSummarizer(Player.PLAYER1)
                    .apply(four));
    Assert.assertEquals(Integer.valueOf(0),
            new TotalPointsSummarizer(Player.PLAYER2)
                    .apply(four));
  }

  @Test
  public void testRandom() {
    HexPlane<Player> four = new HexPlaneImpl<>(4);
    Integer count = 0;
    Random random = new Random();
    for (HexPlaneCoord coord : four.getKnownCoords()) {
      if (random.nextInt(3) == 0) {
        four.setElementAt(coord, Player.PLAYER1);
        count++;
        continue;
      }
      if (random.nextInt(3) == 0) {
        four.setElementAt(coord, Player.PLAYER2);
      }
    }
    Assert.assertEquals(count,
            new TotalPointsSummarizer(Player.PLAYER1)
                    .apply(four));
  }
}
