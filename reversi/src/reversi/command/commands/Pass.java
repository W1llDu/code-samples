package reversi.command.commands;

import java.awt.event.KeyEvent;
import java.util.Objects;

import reversi.command.CommandAggregator;
import reversi.command.CommandFactory;
import reversi.command.CommandIdentifier;
import reversi.command.ModelCommand;
import reversi.command.UnifiedCommand;
import reversi.controller.Player;
import reversi.exceptions.IllegalCommandException;
import reversi.exceptions.MoveOutOfBoundsException;
import reversi.model.MutableReversiModel;

/**
 * A {@link Pass} is a command that passes a move for a given player. The player is given to the
 * command when the command is executed. It's command name is {@code pass}, and its default
 * keybinding is {@code p}.
 */
public class Pass implements ModelCommand {
  static {
    CommandAggregator.registerSelf(new CommandIdentifier("pass", KeyEvent.VK_P),
        new CommandFactory(
            new UnifiedCommand(new Pass()))
    );
  }

  /**
   * Perform the action on the model as a specific player.
   *
   * @param model  the model the action is performed on
   * @param player the player passing
   * @throws IllegalCommandException if it is not the players turn or if the pass is illegal
   *                                 somehow
   * @throws NullPointerException    if any parameter is null
   */
  @Override
  public void runCommandAs(MutableReversiModel model, Player player)
      throws IllegalCommandException, NullPointerException {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    try {
      model.passAsPlayer(player);
    } catch (MoveOutOfBoundsException | IllegalStateException ex) {
      throw new IllegalCommandException("Pass command failed\n" + ex.getMessage(), ex);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return o != null && getClass() == o.getClass();
  }
}
