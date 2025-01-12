package reversi.controller.agent;

import reversi.command.ModelCommand;
import reversi.command.UnifiedCommand;
import reversi.command.ViewCommand;
import reversi.controller.Player;

/**
 * A {@link GraphicalPlayerAgent} is an Agent intended to play graphical-based games of Reversi.
 * This agent often returns commands that are totally empty, since the player has not input any
 * commands to be performed.
 */
public class GraphicalPlayerAgent implements AbsoluteAgent {
  private final Player player;
  private ModelCommand modelCmd;
  private ViewCommand viewCmd;

  /**
   * Create a new {@link GraphicalPlayerAgent} given the player to play as.
   *
   * @param player the player this agent represents
   */
  public GraphicalPlayerAgent(Player player) {
    this.player = player;
  }

  @Override
  public UnifiedCommand getAndResetCommand() {
    if (modelCmd != null && viewCmd != null) {
      return new UnifiedCommand(modelCmd, viewCmd);
    } else if (modelCmd != null) {
      return new UnifiedCommand(modelCmd);
    } else if (viewCmd != null) {
      return new UnifiedCommand(viewCmd);
    }
    return new UnifiedCommand();
  }

  @Override
  public boolean viewIsVisible() {
    return true;
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }
}
