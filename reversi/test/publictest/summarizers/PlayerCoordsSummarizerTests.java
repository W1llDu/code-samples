package publictest.summarizers;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.PlayerCoordsSummarizer;

/**
 * Tests for {@link PlayerCoordsSummarizer}.
 */
public class PlayerCoordsSummarizerTests {

  @Test
  public void testEmpty() {
    HexPlane<Player> four = new HexPlaneImpl<>(4);
    Assert.assertEquals(new HashSet<>(),
            new PlayerCoordsSummarizer(Player.PLAYER1)
                    .apply(four));
    Assert.assertEquals(new HashSet<>(),
            new PlayerCoordsSummarizer(Player.PLAYER2)
                    .apply(four));
  }

  @Test
  public void testRandom() {
    HexPlane<Player> four = new HexPlaneImpl<>(4);
    HashSet<HexPlaneCoord> p1coords = new HashSet<>();
    Random random = new Random();
    for (HexPlaneCoord coord : four.getKnownCoords()) {
      if (random.nextInt(3) == 0) {
        four.setElementAt(coord, Player.PLAYER1);
        p1coords.add(coord);
        continue;
      }
      if (random.nextInt(3) == 0) {
        four.setElementAt(coord, Player.PLAYER2);
      }
    }
    Assert.assertEquals(p1coords,
            new PlayerCoordsSummarizer(Player.PLAYER1)
                    .apply(four));
  }
}
