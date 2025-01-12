package reversi.hex.summarizer.reversiinfo;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link PlayerCoordsSummarizer} summarizes the set of coordinates a specific player has pieces
 * at.
 */
public class PlayerCoordsSummarizer implements HexPlaneSummarizer<Player, Set<HexPlaneCoord>> {
  private final Player player;

  /**
   * Create a new {@link PlayerCoordsSummarizer} given the player to find all the pieces of.
   *
   * @param player the player whose pieces this summarizer should tally
   * @throws NullPointerException if the player is null
   */
  public PlayerCoordsSummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public Set<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return plane.getPlane().entrySet().stream()
        .filter((entry) ->
            entry.getValue().isPresent() && entry.getValue().get() == this.player)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }
}
