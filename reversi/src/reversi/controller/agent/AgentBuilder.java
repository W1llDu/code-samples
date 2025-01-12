package reversi.controller.agent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import reversi.command.UnifiedCommand;
import reversi.command.commands.Pass;
import reversi.controller.Player;

/**
 * A Builder for {@link Agent}s. Optional-returning agents are stacked upon one another until a
 * finalizing strategy is requested, in which case no more strategies can be appended.
 */
public final class AgentBuilder {

  private AgentBuilder() {

  }

  public static MaybeAgentBuilder create(MaybeAgent agent) {
    return new MaybeAgentBuilder(Objects.requireNonNull(agent));
  }


  public static FinalizedAgentBuilder create(AbsoluteAgent agent) {
    return new FinalizedAgentBuilder(Objects.requireNonNull(agent));
  }

  /**
   * A Builder for stacking Optionallly-returning agents.
   */
  public static final class MaybeAgentBuilder {
    private final MaybeAgent agent;

    private MaybeAgentBuilder(MaybeAgent agent) {
      this.agent = Objects.requireNonNull(agent);
    }

    /**
     * Combine a {@link MaybeAgent} with another MaybeAgent. The provided MaybeAgent will be used as
     * a fallback in the case the primary agent fails to provide a valid move.
     *
     * @param next the fallback agent
     * @return a new {@link MaybeAgentBuilder}
     */
    public MaybeAgentBuilder andThen(MaybeAgent next) {
      Objects.requireNonNull(next);

      if (agent.getPlayer() != next.getPlayer()) {
        throw new IllegalArgumentException("Agent player must be the same as the prior");
      }

      // used to put two strategies together
      class AndThenStrategy implements MaybeAgent {
        private final MaybeAgent primary;
        private final MaybeAgent secondary;

        AndThenStrategy(MaybeAgent primary, MaybeAgent secondary) {
          this.primary = Objects.requireNonNull(primary);
          this.secondary = Objects.requireNonNull(secondary);
        }

        @Override
        public List<UnifiedCommand> getAndResetCommand() {
          List<UnifiedCommand> primCmd = primary.getAndResetCommand();
          if (primCmd.isEmpty()) {
            return secondary.getAndResetCommand();
          }
          return primCmd;
        }

        @Override
        public boolean viewIsVisible() {
          return primary.viewIsVisible();
        }

        @Override
        public Player getPlayer() {
          return primary.getPlayer();
        }
      }

      return new MaybeAgentBuilder(new AndThenStrategy(this.agent, next));
    }

    /**
     * Like {@link #andThen(MaybeAgent)}, but will attempt to unify the commands into only those
     * that match. Note that AI Agents will return lists of <b>all</b> possible moves; for example,
     * the {@link GreedyAIAgent} will return a sorted list with the moves producing the most points
     * first in the list. That means unifying it with the {@link PasteAIAgent} will result in the
     * worst move being chosen. Reversing the unification order will result in the opposite effect.
     */
    public MaybeAgentBuilder andThenUnify(MaybeAgent next) {
      Objects.requireNonNull(next);

      class AndThenUnify implements MaybeAgent {
        private final MaybeAgent primary;
        private final MaybeAgent secondary;

        AndThenUnify(MaybeAgent primary, MaybeAgent secondary) {
          this.primary = Objects.requireNonNull(primary);
          this.secondary = Objects.requireNonNull(secondary);
        }

        @Override
        public List<UnifiedCommand> getAndResetCommand() {
          List<UnifiedCommand> primCmds = primary.getAndResetCommand();
          List<UnifiedCommand> secdCmds = secondary.getAndResetCommand();

          List<UnifiedCommand> matching = primCmds.stream()
              .filter(cmd -> secdCmds.stream().anyMatch(snd -> snd.equals(cmd)))
              .collect(Collectors.toList());

          if (matching.isEmpty()) {
            return primCmds;
          }

          return matching;
        }

        @Override
        public boolean viewIsVisible() {
          return primary.viewIsVisible();
        }

        @Override
        public Player getPlayer() {
          return primary.getPlayer();
        }
      }

      return new MaybeAgentBuilder(new AndThenUnify(agent, next));
    }

    /**
     * Finalize the {@link MaybeAgentBuilder} by returning an Agent that will throw should no move
     * options be available. This is a terminal operation.
     *
     * @return a {@link FinalizedAgentBuilder}
     */
    public FinalizedAgentBuilder getFirstMoveOrPassFinalizer() {
      class GetFirstMoveOrPassStrategy implements AbsoluteAgent {
        private final MaybeAgent agent;

        GetFirstMoveOrPassStrategy(MaybeAgent agent) {
          this.agent = Objects.requireNonNull(agent);
        }

        @Override
        public UnifiedCommand getAndResetCommand() {
          List<UnifiedCommand> command = agent.getAndResetCommand();
          if (command.isEmpty()) {
            return new UnifiedCommand(new Pass());
          }
          return command.get(0);
        }

        @Override
        public boolean viewIsVisible() {
          return agent.viewIsVisible();
        }

        @Override
        public Player getPlayer() {
          return agent.getPlayer();
        }
      }

      return new FinalizedAgentBuilder(new GetFirstMoveOrPassStrategy(agent));
    }
  }

  /**
   * A Builder for finalizing the strategy, in which case no more strategies can be appended.
   */
  public static final class FinalizedAgentBuilder {
    private final AbsoluteAgent agent;

    private FinalizedAgentBuilder(AbsoluteAgent agent) {
      this.agent = Objects.requireNonNull(agent);
    }

    public AbsoluteAgent build() {
      return agent;
    }
  }
}
