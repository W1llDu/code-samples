package reversi.controller.agent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import reversi.command.UnifiedCommand;
import reversi.command.commands.Move;
import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.summarizer.HexPlaneSummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * An Agent useful for AI. Uses a specific Strategy to choose a move. Any AI <b>must</b> inherit
 * this class. Correct behavior is not guaranteed otherwise. Notably, AI must not request the view
 * be shown.
 */
public abstract class AbstractAIAgent implements MaybeAgent {
  protected final Player player;
  protected final ReadOnlyReversiModel<Player> model;
  protected HexPlaneSummarizer<Player, List<HexPlaneCoord>> strategy;

  /**
   * Create a new {@link AbstractAIAgent} given the player the agent belongs to and a reference to
   * the model. The strategy used should order the preferred results from most preferred to least
   * preferred.
   *
   * @param player   the player this Agent should play as
   * @param model    the model representing the board state
   * @param strategy the strategy to determine the moves from best to worst
   * @throws NullPointerException if any argument is null
   */
  protected AbstractAIAgent(Player player,
                            ReadOnlyReversiModel<Player> model,
                            HexPlaneSummarizer<Player, List<HexPlaneCoord>> strategy) {
    this.player = Objects.requireNonNull(player);
    this.model = Objects.requireNonNull(model);
    this.strategy = Objects.requireNonNull(strategy);
  }

  @Override
  public List<UnifiedCommand> getAndResetCommand() {
    List<HexPlaneCoord> move = strategy.apply(model.getHexPlane());
    return move.stream()
        .map(coord -> new UnifiedCommand(new Move(coord)))
        .collect(Collectors.toList());
  }

  @Override
  public final boolean viewIsVisible() {
    return false;
  }

  @Override
  public final Player getPlayer() {
    return player;
  }
}
