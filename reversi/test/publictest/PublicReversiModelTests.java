package publictest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.PlayerCoordsSummarizer;
import reversi.model.ReversiModel;

/**
 * Tests for the public facing interface of {@link ReversiModel}.
 */
public class PublicReversiModelTests {

  private ReversiModel four;

  @Before
  public void init() {
    four = new ReversiModel(4);
  }

  @After
  public void cleanup() {
    // Check the invariant
    assertNoNulls();
  }

  @Test
  public void testConstructor() {
    Assert.assertThrows(IllegalArgumentException.class,
        () -> new ReversiModel(0));
    ReversiModel empty = new ReversiModel(1);
    Assert.assertEquals(empty.getHexPlane(),
            new HexPlaneImpl<Player>(1));

    ReversiModel medium = new ReversiModel(new Random().nextInt(3) + 3);
    Assert.assertEquals(6,
            medium.getHexPlane()
                    .getPlane().values().stream()
                    .filter(Optional::isPresent).count());
    HashSet<HexPlaneCoord> p1coords = new HashSet<>();
    HashSet<HexPlaneCoord> p2coords = new HashSet<>();
    p2coords.add(new CubicalCoordinate(1, -1, 0));
    p1coords.add(new CubicalCoordinate(1, 0, -1));
    p2coords.add(new CubicalCoordinate(0, 1, -1));
    p1coords.add(new CubicalCoordinate(-1, 1, 0));
    p2coords.add(new CubicalCoordinate(-1, 0, 1));
    p1coords.add(new CubicalCoordinate(0, -1, 1));
    Assert.assertEquals(p1coords,
        new PlayerCoordsSummarizer(Player.PLAYER1).apply(medium.getHexPlane()));
    Assert.assertEquals(p2coords,
        new PlayerCoordsSummarizer(Player.PLAYER2).apply(medium.getHexPlane()));
  }

  @Test
  public void testMoveNull() {
    Assert.assertThrows(NullPointerException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(1, 1), null));
    Assert.assertThrows(NullPointerException.class,
        () -> four.makeMoveAsPlayer(null, Player.PLAYER1));
  }

  // oob coordinate
  @Test
  public void testMoveOOB() {
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(6, 6), Player.PLAYER1));
  }

  // wrong player's turn
  @Test
  public void testMoveWrongPlayer() {
    // valid move, wrong player
    Assert.assertThrows(IllegalStateException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(1, -2), Player.PLAYER2));
  }

  // valid move + flipped pieces
  @Test
  public void testMoveValidFlip() {
    // valid move
    four.makeMoveAsPlayer(new AxialCoordinate(1, -2), Player.PLAYER1);
    Assert.assertEquals(Optional.of(Player.PLAYER1),
            four.getAtHex(new AxialCoordinate(1, -1)));
    Assert.assertEquals(Optional.of(Player.PLAYER1),
            four.getAtHex(new AxialCoordinate(1, -2)));
  }

  // invalid move + zero flipped pieces
  @Test
  public void testMoveInvalidNoFlip() {
    // invalid move
    Assert.assertThrows(IllegalStateException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(1, -1), Player.PLAYER1));
    Assert.assertEquals(Optional.of(Player.PLAYER2),
            four.getAtHex(new AxialCoordinate(1, -1)));
    Assert.assertThrows(IllegalStateException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(2, -2), Player.PLAYER1));
    Assert.assertEquals(Optional.empty(),
            four.getAtHex(new AxialCoordinate(2, -2)));
  }

  @Test
  public void testGetPlayer() {
    Assert.assertEquals(Player.PLAYER1,
            four.getPlayer());
    // invalid move
    Assert.assertThrows(IllegalStateException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(1, -1), Player.PLAYER1));
    // valid move
    four.makeMoveAsPlayer(new AxialCoordinate(1, -2), Player.PLAYER1);
    Assert.assertEquals(Player.PLAYER2,
            four.getPlayer());

    // valid move
    four.makeMoveAsPlayer(new AxialCoordinate(-1, 2), Player.PLAYER2);
    Assert.assertEquals(Player.PLAYER1,
            four.getPlayer());
    // invalid move
    Assert.assertThrows(IllegalStateException.class,
        () -> four.makeMoveAsPlayer(new AxialCoordinate(2, -2), Player.PLAYER1));
    Assert.assertEquals(Player.PLAYER1,
            four.getPlayer());
  }

  @Test
  public void testGetAtHex() {
    Assert.assertEquals(Optional.of(Player.PLAYER1),
            four.getAtHex(new AxialCoordinate(0, -1)));
    Assert.assertEquals(Optional.of(Player.PLAYER2),
            four.getAtHex(new AxialCoordinate(0, 1)));
    Assert.assertEquals(Optional.empty(),
            four.getAtHex(new AxialCoordinate(0, 0)));
    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> four.getAtHex(new AxialCoordinate(6, 6)));
    Assert.assertThrows(NullPointerException.class,
        () -> four.getAtHex(null));
  }

  private void assertNoNulls() {
    HexPlane<Player> plane = four.getHexPlane();
    for (HexPlaneCoord coord : plane.getKnownCoords()) {
      Assert.assertNotNull(four.getAtHex(coord));
    }
  }
}
