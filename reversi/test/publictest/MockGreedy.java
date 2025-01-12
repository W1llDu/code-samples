package publictest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import reversi.command.UnifiedCommand;
import reversi.command.commands.Move;
import reversi.controller.Player;
import reversi.controller.agent.GreedyAIAgent;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.strategy.GreedyStrategySummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * Utility mocking class.
 */
public class MockGreedy extends GreedyAIAgent {

  public final Appendable log;

  /**
   * Create a new {@link GreedyAIAgent} given the player the agent belongs to and a reference
   * to the model.
   *
   * @param player the player this Agent should play as
   * @param model  the model representing the board state
   */
  public MockGreedy(Player player, ReadOnlyReversiModel<Player> model, Appendable log) {
    super(player, model);
    strategy = new MockGreedySummarizer(player);
    this.log = log;
  }

  @Override
  public List<UnifiedCommand> getAndResetCommand() {
    List<HexPlaneCoord> move = new MockGreedySummarizer(player).apply(model.getHexPlane());
    return move.stream()
        .map(coord -> new UnifiedCommand(new Move(coord)))
        .collect(Collectors.toList());
  }

  private class MockGreedySummarizer extends GreedyStrategySummarizer {

    /**
     * Create a new {@link GreedyStrategySummarizer} given the player this summarizer uses to
     * calculate move points.
     *
     * @param player the player used to calculate the points gained from moves
     */
    public MockGreedySummarizer(Player player) {
      super(player);
    }

    @Override
    public List<HexPlaneCoord> apply(HexPlane<Player> plane)
            throws NullPointerException, IndexOutOfBoundsException {
      List<HexPlaneCoord> inspected = super.apply(plane);
      for (HexPlaneCoord coord : inspected) {
        try {
          log.append("Accessed: ");
          log.append(coord.toString());
          log.append("\n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return inspected;
    }
  }
}
