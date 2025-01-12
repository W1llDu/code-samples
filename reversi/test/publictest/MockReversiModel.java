package publictest;

import java.util.Objects;
import java.util.Optional;

import reversi.controller.Player;
import reversi.exceptions.MoveOutOfBoundsException;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;

/**
 * A MockReversiModel implements the game logic behind a game of Reversi
 * and logs actions on the model.
 */
public class MockReversiModel implements MutableReversiModel {
  private final ReversiModel model;
  private final StringBuilder log;

  /**
   * Constructor for MockReversiModel.
   *
   * @param plane the board.
   * @param player the active player.
   * @param log the log.
   */
  public MockReversiModel(HexPlane<Player> plane, Player player, StringBuilder log) {
    this.model = new ReversiModel(new MockHexPlane<>(Objects.requireNonNull(plane), log),
            Objects.requireNonNull(player));
    this.log = Objects.requireNonNull(log);
  }

  public MockReversiModel(int radius, StringBuilder log) {
    this.model = new ReversiModel(radius);
    this.log = log;
  }

  @Override
  public void makeMoveAsPlayer(HexPlaneCoord coordinate, Player player)
          throws MoveOutOfBoundsException, IllegalStateException {
    log.append(String.format("Move at %s as %s.\n", coordinate, player));
    model.makeMoveAsPlayer(coordinate, player);
  }

  @Override
  public void passAsPlayer(Player player) throws IllegalStateException {
    log.append(String.format("Pass as %s.\n", player));
    model.passAsPlayer(player);
  }

  @Override
  public HexPlane<Player> getHexPlane() {
    return model.getHexPlane();
  }

  @Override
  public Player getPlayer() {
    log.append("Get active player.\n");
    return model.getPlayer();
  }

  @Override
  public Optional<Player> getAtHex(HexPlaneCoord coordinate)
          throws IndexOutOfBoundsException, NullPointerException {
    log.append(String.format("Get player at %s.\n", coordinate));
    return model.getAtHex(coordinate);
  }
}
