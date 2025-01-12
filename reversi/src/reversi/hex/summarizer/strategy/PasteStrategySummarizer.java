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
 * The {@link PasteStrategySummarizer} is the opposite of the {@link GreedyStrategySummarizer};
 * instead of playing the most points, it players the move yielding the least points. Please be nice
 * to {@link PasteStrategySummarizer}, as it ate paste when it was a small, young Summarizer.
 */
public class PasteStrategySummarizer
    implements HexPlaneSummarizer<Player, List<HexPlaneCoord>> {
  private final Player player;

  /**
   * Create a new {@link PasteStrategySummarizer} given the player to use to determine the
   * least-scoring move for.
   *
   * @param player the player for whom to choose the lowest scoring move
   */
  public PasteStrategySummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public List<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    List<HexPlaneCoord> moves = new PlayerPossibleMovesSummarizer(player).apply(plane);
    // when moves are added to retList the uppermost-leftmost coordinate is added first.

    return moves.stream()
        .sorted((coord1, coord2) ->
            new PointsFromMoveAtPointSummarizer(coord1, player).apply(plane)
                - new PointsFromMoveAtPointSummarizer(coord2, player).apply(plane))
        .collect(Collectors.toList());
  }
}
