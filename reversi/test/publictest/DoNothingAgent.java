package publictest;

import java.util.Objects;

import reversi.command.UnifiedCommand;
import reversi.command.commands.Pass;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;

/**
 * Used for testing. It is an {@link AbsoluteAgent} that always passes.
 */
public class DoNothingAgent implements AbsoluteAgent {
  private final Player player;

  /**
   * Create a new {@link DoNothingAgent} given the player to act as. This Agent will always pass.
   *
   * @param player the player to act as
   */
  public DoNothingAgent(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public UnifiedCommand getAndResetCommand() {
    return new UnifiedCommand(new Pass());
  }

  @Override
  public boolean viewIsVisible() {
    return false;
  }

  @Override
  public Player getPlayer() {
    return player;
  }
}
