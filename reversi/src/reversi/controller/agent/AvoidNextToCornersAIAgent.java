package reversi.controller.agent;

import reversi.controller.Player;
import reversi.hex.summarizer.strategy.AvoidNextToCornersStrategySummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * A utility Agent meant to make other strategies smarter. It will avoid moves that are next to
 * corners to avoid allowing the opponent to make a move with an invulnerable piece. Note that if
 * this is unified with an Agent that doesn't avoid near-corners, it may result in a near-corner
 * move by lack of any better move.
 */
public class AvoidNextToCornersAIAgent extends AbstractAIAgent {

  /**
   * Create a new {@link AvoidNextToCornersAIAgent} given the player the agent belongs to and a
   * reference to the model.
   *
   * @param player the player this Agent should play as
   * @param model  the model representing the board state
   */
  public AvoidNextToCornersAIAgent(Player player, ReadOnlyReversiModel<Player> model) {
    super(player, model, new AvoidNextToCornersStrategySummarizer(player));
  }
}
