package reversi.hex.summarizer.reversiinfo;

import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link PlayerHasLegalMovesSummarizer} summarizes whether a given player has legal moves
 * availible.
 */
public class PlayerHasLegalMovesSummarizer implements HexPlaneSummarizer<Player, Boolean> {
  private final Player player;

  /**
   * Create a new {@link PlayerHasLegalMovesSummarizer}, which will use the supplied coordinates to
   * calculate its summarization.
   *
   * @param player the player to use in calculations for points
   * @throws NullPointerException if any argument is null
   */
  public PlayerHasLegalMovesSummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public Boolean apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return !new PlayerPossibleMovesSummarizer(player).apply(plane).isEmpty();
  }
}
