package reversi.controller.agent;

import reversi.controller.Player;
import reversi.hex.summarizer.strategy.PasteStrategySummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * A Reverse-Greedy agent. The Paste strategy will choose the worst move at any given instance. If
 * there are two equally bad moves, the leftmost topmost worst move will be picked.
 */
public class PasteAIAgent extends AbstractAIAgent {

  /**
   * Create a new {@link PasteAIAgent} given the player the agent belongs to and a reference to the
   * model.
   *
   * @param player the player this Agent should play as
   * @param model  the model representing the board state
   */
  public PasteAIAgent(Player player, ReadOnlyReversiModel<Player> model) {
    super(player, model, new PasteStrategySummarizer(player));
  }
}
