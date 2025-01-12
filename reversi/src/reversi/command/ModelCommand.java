package reversi.command;

import reversi.controller.Player;
import reversi.exceptions.IllegalCommandException;
import reversi.model.MutableReversiModel;

/**
 * A {@link ModelCommand} represents an action to perform on the model using a
 * {@link MutableReversiModel}. Commands that wish to be usable by the players ought to register
 * themselves with the {@link CommandAggregator}. Implementers are obligated to override
 * {@link Object#equals(Object)} and {@link Object#toString()}.
 */
public interface ModelCommand {
  /**
   * Perform the action on the model as a specific player. Throwing an exception is well-defined,
   * but it must be specified in the documentation and in a {@code throws} clause.
   *
   * @param model  the model the action is performed on
   * @param player the player the action is performed as
   * @throws IllegalCommandException if the command cannot be completed
   * @throws NullPointerException    if model is null
   * @implSpec commands ought to exhibit well-defined behavior should {@code player} be null.
   */
  void runCommandAs(MutableReversiModel model, Player player)
      throws IllegalCommandException, NullPointerException;
}
