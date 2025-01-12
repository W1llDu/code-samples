package reversi.hex.summarizer.reversiinfo;

import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link PointsFromMoveAtPointSummarizer} will summarize the points of a potential move at a
 * specific spot for a given player.
 */
public class PointsFromMoveAtPointSummarizer implements HexPlaneSummarizer<Player, Integer> {
  private final HexPlaneCoord coordinate;
  private final Player player;

  /**
   * Create a new {@link PointsFromMoveAtPointSummarizer}, which will use the supplied coordinates
   * to calculate its summarization.
   *
   * @param coordinate the coordinate to find points for a particular move
   * @param player     the player to use in calculations for points
   * @throws NullPointerException if any argument is null
   */
  public PointsFromMoveAtPointSummarizer(HexPlaneCoord coordinate, Player player) {
    this.coordinate = Objects.requireNonNull(coordinate);
    this.player = Objects.requireNonNull(player);
  }


  @Override
  public Integer apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    Objects.requireNonNull(plane);
    if (!plane.getKnownCoords().contains(coordinate)) {
      return 0;
      //throw new IndexOutOfBoundsException("PointsFromMoveAtPointSummarizer: " +
      //  "coordinate is out of bounds: " + coordinate);
    }
    return new CoordsToFlipGivenMoveSummarizer(coordinate, player).apply(plane).size();
  }
}
