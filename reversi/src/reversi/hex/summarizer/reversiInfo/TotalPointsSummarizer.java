package reversi.hex.summarizer.reversiinfo;

import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link TotalPointsSummarizer} will summarize the total points of a player.
 */
public class TotalPointsSummarizer implements HexPlaneSummarizer<Player, Integer> {
  private final Player player;

  /**
   * Create a new {@link TotalPointsSummarizer}, which will use the player to calculate its
   * summarization.
   *
   * @param player the player to use in calculations for points
   * @throws NullPointerException if any argument is null
   */
  public TotalPointsSummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public Integer apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return new PlayerCoordsSummarizer(this.player).apply(plane).size();
  }
}
