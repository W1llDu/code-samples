package reversi.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import reversi.controller.Player;
import reversi.exceptions.MoveOutOfBoundsException;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.hex.summarizer.reversiinfo.CoordsToFlipGivenMoveSummarizer;

/**
 * A ReversiModel implements the game logic behind a game of Reversi. It is the Model of the Model
 * View Controller architecture.
 *
 * <p>Upon any invalid moves, the Model will throw an exception.
 *
 * <p>It is recommended to avoid passing Mutable instances of this class where possible; if a
 * {@code ReadOnlyReversiModel<Player>} suffices, avoid mentioning {@link MutableReversiModel}.
 */
public class ReversiModel implements MutableReversiModel {
  /**
   * Invariant: hexPlane contains no nulls.
   */
  private final HexPlane<Player> hexPlane;
  private Player curPlayer;

  /**
   * Create a new ReversiModel given the radius of the board. By default, Player1 starts. The board
   * starts with the center hex empty. Surrounding it, starting at (1, -1, 0) and going clockwise,
   * is Player2, Player1, Player2, Player1, Player2, and Player1 (i.e. the opposite positions on the
   * hex surrounding the center belong to opposite players). For clarification, consider the
   * following diagram:
   * <pre>
   * {@code
   *   X O
   *  O _ X
   *   X O
   * }
   * </pre>
   *
   * @param radius the radius of the game board
   * @throws IllegalArgumentException if radius is 0 or less
   */
  public ReversiModel(int radius) {
    if (radius < 1) {
      throw new IllegalArgumentException("Radius passed to ReversiModel must be positive.");
    }
    this.hexPlane = new HexPlaneImpl<>(radius);
    // initial state
    this.curPlayer = Player.PLAYER1;
    // only create starting ring if radius > 1
    if (radius == 1) {
      return;
    }
    Player temp = Player.PLAYER2;
    for (HexPlaneCoord coord : Arrays.asList(
        new CubicalCoordinate(1, -1, 0),
        new CubicalCoordinate(1, 0, -1),
        new CubicalCoordinate(0, 1, -1),
        new CubicalCoordinate(-1, 1, 0),
        new CubicalCoordinate(-1, 0, 1),
        new CubicalCoordinate(0, -1, 1))) {
      this.hexPlane.setElementAt(coord, temp);
      temp = temp.getOther();
    }
  }

  /**
   * Create a new {@link ReversiModel} with the given radius and starting player. For detailed
   * documentation, see {@link #ReversiModel(int)}.
   *
   * @param radius      the radius of the game board
   * @param firstPlayer the player to make the first move
   * @throws NullPointerException if the player is null
   */
  public ReversiModel(int radius, Player firstPlayer) {
    this(radius);
    this.curPlayer = Objects.requireNonNull(firstPlayer);
  }

  /**
   * Create a new ReversiModel given the current board state and active player.
   *
   * @param hexPlane the board
   * @param player   the current active player
   */
  public ReversiModel(HexPlane<Player> hexPlane, Player player) {
    this.hexPlane = new HexPlaneImpl<>(hexPlane);
    this.curPlayer = Objects.requireNonNull(player);
  }

  @Override
  public void makeMoveAsPlayer(HexPlaneCoord coordinate, Player player)
      throws MoveOutOfBoundsException, IllegalStateException {
    Objects.requireNonNull(coordinate);
    Objects.requireNonNull(player);
    if (!player.equals(curPlayer)) {
      throw new IllegalStateException("It is not " + player + "'s turn, cannot make an action.");
    }
    if (!this.hexPlane.getKnownCoords().contains(coordinate)) {
      throw new MoveOutOfBoundsException("MoveOutOfBoundsException: " + coordinate);
    }
    if (this.hexPlane.getAtHex(coordinate).isPresent()) {
      throw new IllegalStateException("Invalid move: can't overwrite existing piece.");
    }
    List<HexPlaneCoord> toChange = new CoordsToFlipGivenMoveSummarizer(coordinate, player)
        .apply(new HexPlaneImpl<>(this.hexPlane));
    // no points to change: invalid move
    if (toChange.size() == 1) {
      throw new IllegalStateException("Invalid move: attempted move does not flip any pieces.");
    }
    for (HexPlaneCoord coord : toChange) {
      this.hexPlane.setElementAt(coord, player);
    }
    this.curPlayer = curPlayer.getOther();
  }

  @Override
  public void passAsPlayer(Player player) throws IllegalStateException {
    Objects.requireNonNull(player);
    if (!player.equals(curPlayer)) {
      throw new IllegalStateException("It is not " + player + "'s turn, cannot make an action.");
    }
    this.curPlayer = curPlayer.getOther();
  }

  @Override
  public Player getPlayer() {
    return curPlayer;
  }

  @Override
  public Optional<Player> getAtHex(HexPlaneCoord coordinate)
      throws MoveOutOfBoundsException {
    try {
      return this.hexPlane.getAtHex(coordinate);
    } catch (IndexOutOfBoundsException e) {
      throw new MoveOutOfBoundsException("ReversiModel: " +
          "coordinate is out of bounds: " + coordinate);
    }
  }

  @Override
  public HexPlane<Player> getHexPlane() {
    return new HexPlaneImpl<>(this.hexPlane);
  }
}
