package publictest;

import java.util.Iterator;

import reversi.command.ModelCommand;
import reversi.command.UnifiedCommand;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;

/**
 * A {@link SetMovesAgent} is a testing class that uses an Iterator to select moves.
 * Provided an Iterator of {@link ModelCommand}s, it will supply each Command once upon request.
 */
public class SetMovesAgent implements AbsoluteAgent {
  private final Player player;
  private final Iterator<ModelCommand> moves;

  /**
   * Create a new {@link SetMovesAgent} given the player to play as and the source for moves to
   * supply.
   *
   * @param player the player to play as
   * @param moves the source of moves
   */
  public SetMovesAgent(Player player, Iterator<ModelCommand> moves) {
    this.player = player;
    this.moves = moves;
  }

  @Override
  public UnifiedCommand getAndResetCommand() {
    return new UnifiedCommand(moves.next());
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
