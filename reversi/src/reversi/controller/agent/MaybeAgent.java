package reversi.controller.agent;

import java.util.List;

import reversi.command.UnifiedCommand;

/**
 * A {@link MaybeAgent} represents a strategy to generate a Reversi move. The strategy may fail, in
 * which case it returns an empty list.
 *
 * @see AbsoluteAgent
 * @see Agent
 */
public interface MaybeAgent extends Agent<List<UnifiedCommand>> {
}
