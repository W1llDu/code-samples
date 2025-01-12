package reversi.controller.agent;

import reversi.command.UnifiedCommand;

/**
 * An {@link AbsoluteAgent} is an {@link Agent} that always returns a value. For example, a player
 * in a synchronous environment will always return a value.
 *
 * @see MaybeAgent
 * @see Agent
 */
public interface AbsoluteAgent extends Agent<UnifiedCommand> {
}
