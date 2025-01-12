package reversi.hex.summarizer.reversiinfo;

import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link IsMoveLegalSummarizer} summarizes whether a move at a given spot for a given player is
 * legal.
 */
public class IsMoveLegalSummarizer implements HexPlaneSummarizer<Player, Boolean> {
  private final HexPlaneCoord coordinate;
  private final Player player;

  /**
   * Create a new {@link IsMoveLegalSummarizer}, which will use the supplied coordinates to
   * calculate its summarization.
   *
   * @param coordinate the coordinate to find points for a particular move
   * @param player     the player to use in calculations for points
   * @throws NullPointerException if any argument is null
   */
  public IsMoveLegalSummarizer(HexPlaneCoord coordinate, Player player) {
    this.coordinate = Objects.requireNonNull(coordinate);
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public Boolean apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return new PointsFromMoveAtPointSummarizer(coordinate, player).apply(plane) > 1;
  }
}
