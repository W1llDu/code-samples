package reversi.hex.summarizer.reversiinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * The {@link PlayerPossibleMovesSummarizer} will summarize the possible moves the player can make.
 * The moves are returned in order from topmost left to bottommost right.
 */
public class PlayerPossibleMovesSummarizer
    implements HexPlaneSummarizer<Player, List<HexPlaneCoord>> {
  private final Player player;

  /**
   * Create a new {@link PlayerPossibleMovesSummarizer} given the {@link Player} to summarize.
   *
   * @param player the player to get the possible moves for
   * @throws NullPointerException if the player is null
   */
  public PlayerPossibleMovesSummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public List<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    List<HexPlaneCoord> points = new ArrayList<>();
    for (HexPlaneCoord coord : plane.getKnownCoords()) {
      if (new IsMoveLegalSummarizer(coord, player).apply(plane)) {
        points.add(coord);
      }
    }
    // sort so that the first move is the uppermost-leftmost coordinate
    // (-, -) is top left
    points.sort((h1, h2) -> h1.getQ() + h1.getR() == h2.getQ() + h2.getR()
        ? h1.getR() - h2.getR() // get uppermost in diagonal
        : h1.getQ() + h1.getR() - h2.getQ() - h2.getR()); // get uppermost-leftmost
    return points;
  }
}
