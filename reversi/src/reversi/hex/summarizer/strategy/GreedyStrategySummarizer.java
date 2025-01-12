package reversi.hex.summarizer.strategy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;
import reversi.hex.summarizer.reversiinfo.PlayerPossibleMovesSummarizer;
import reversi.hex.summarizer.reversiinfo.PointsFromMoveAtPointSummarizer;

/**
 * A Summarizer for use in implementing a Greedy agent. A Greedy strategy is one where the move with
 * the highest total points is picked. In the case of {@link GreedyStrategySummarizer}, if more than
 * a single move results in the highest number of points, then the move closest to the top left is
 * chosen.
 */
public class GreedyStrategySummarizer
    implements HexPlaneSummarizer<Player, List<HexPlaneCoord>> {
  private final Player player;

  /**
   * Create a new {@link GreedyStrategySummarizer} given the player this summarizer uses to
   * calculate move points.
   *
   * @param player the player used to calculate the points gained from moves
   */
  public GreedyStrategySummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public List<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    List<HexPlaneCoord> moves = new PlayerPossibleMovesSummarizer(player).apply(plane);
    // when moves are added to retList the uppermost-leftmost coordinate is added first.

    return moves.stream()
        .sorted(((coord1, coord2) ->
            new PointsFromMoveAtPointSummarizer(coord2, player).apply(plane)
                - new PointsFromMoveAtPointSummarizer(coord1, player).apply(plane)))
        .collect(Collectors.toList());
  }
}
