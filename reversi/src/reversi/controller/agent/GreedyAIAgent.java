package reversi.controller.agent;

import reversi.controller.Player;
import reversi.hex.summarizer.strategy.GreedyStrategySummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * A Greedy agent. A Greedy strategy chooses the move with the highest value. If there are
 * conflicting moves, returns nothing.
 */
public class GreedyAIAgent extends AbstractAIAgent {

  /**
   * Create a new {@link GreedyAIAgent} given the player the agent belongs to and a reference to the
   * model.
   *
   * @param player the player this Agent should play as
   * @param model  the model representing the board state
   */
  public GreedyAIAgent(Player player, ReadOnlyReversiModel<Player> model) {
    super(player, model, new GreedyStrategySummarizer(player));
  }
}
