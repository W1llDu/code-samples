package reversi.command;

import reversi.controller.Player;
import reversi.model.ReadOnlyReversiModel;
import reversi.view.ReversiView;

/**
 * A {@link ViewCommand} represents an action to perform on a view. An example could be giving the
 * view a position to display information. Frequently used with an
 * {@link reversi.controller.agent.AgentBuilder} through a {@link UnifiedCommand}. Implementers are
 * obligated to override {@link Object#equals} and {@link Object#hashCode()}.
 *
 * @see reversi.controller.agent.AgentBuilder
 */
public interface ViewCommand {

  /**
   * Perform the action on the view.
   *
   * @param view the view to perform the action on
   * @throws NullPointerException if the view is null
   */
  void performViewCommand(ReversiView view, ReadOnlyReversiModel<Player> model)
      throws NullPointerException;
}
