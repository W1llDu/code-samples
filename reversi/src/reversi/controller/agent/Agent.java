package reversi.controller.agent;

import reversi.controller.Player;

/**
 * An {@link Agent} represents something that can play Reversi. Usually implementers of this class
 * are AI implementations, but human players have their own Agent as well. An Agent queues a command
 * it would like to perform on the game. An Agent can request commands be performed on both the View
 * and the Model because some actions a player might perform may have very little to do with the
 * Model itself, such as hovering the mouse over a cell, which ought to be highlighted by the View
 * but the Model should not care.
 *
 * <p>Implementers should not implement this interface directly. Please extend either
 * {@link AbsoluteAgent} or {@link MaybeAgent}.
 */
interface Agent<R> {
  /**
   * Returns the command this agent currently has queued. If a command was returned, that command
   * ought to be removed from the Agent's queue.
   *
   * @return the Agent's queued command
   */
  R getAndResetCommand();

  /**
   * Return whether this agent requires the View representing it to be visible. Some Agents, like
   * Players, may want to see their board, while others (e.g. AI) may not require it (although it
   * may use it for the purpose of visualization, among other reasons).
   *
   * @implNote Note that the Controller may choose not to always respect the return value of
   *     this method. For example, if two AI don't require a View, the Controller may choose to
   *     ignore this value and render anyway.
   */
  boolean viewIsVisible();

  /**
   * Returns the {@link Player} this Agent belongs to.
   */
  Player getPlayer();
}
