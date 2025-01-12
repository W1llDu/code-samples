package reversi.controller;

import java.util.Objects;
import java.util.Optional;

import reversi.command.ModelCommand;
import reversi.command.UnifiedCommand;
import reversi.command.ViewCommand;
import reversi.command.commands.Pass;
import reversi.controller.agent.AbsoluteAgent;
import reversi.exceptions.IllegalCommandException;
import reversi.hex.summarizer.reversiinfo.IsGameOverSummarizer;
import reversi.model.MutableReversiModel;
import reversi.view.ReversiView;

/**
 * Implementation of the {@link ReversiController} interface. Relies on subclass's implementation of
 * checking Agents to determine if control should be handed off or if Agents should be checked
 * continuously.
 */
public abstract class AbstractReversiController implements ReversiController {
  protected final MutableReversiModel model;
  protected final AbsoluteAgent agent1;
  protected final AbsoluteAgent agent2;
  protected final ReversiView view1;
  protected final ReversiView view2;

  /**
   * Superclass constructor for Controllers. The Agents are required for obvious reasons, but the
   * Views are more complex. The subclass needn't require the View for its constructor, but should
   * provide one to the superclass anyway. If a single View is used, simply pass the same View to
   * both arguments.
   *
   * @param agent1 the agent for the first player
   * @param agent2 the agent for the second player
   * @param view1  the view for the first player
   * @param view2  the view for the second player
   * @throws NullPointerException if any argument is null
   */
  public AbstractReversiController(AbsoluteAgent agent1,
                                   AbsoluteAgent agent2,
                                   ReversiView view1,
                                   ReversiView view2,
                                   MutableReversiModel model) {
    this.agent1 = Objects.requireNonNull(agent1);
    this.agent2 = Objects.requireNonNull(agent2);
    this.view1 = Objects.requireNonNull(view1);
    this.view2 = Objects.requireNonNull(view2);
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public void startGame() {
    renderViews();
    pollAgents();
  }

  @Override
  public boolean isGameOver() {
    return new IsGameOverSummarizer().apply(model.getHexPlane());
  }

  @Override
  public void accept(UnifiedCommand command, Player player)
      throws NullPointerException {
    Optional<ModelCommand> modelCmd = command.getModelCommand();
    Optional<ViewCommand> viewCmd = command.getViewCommand();

    // send it to the correct view for the corresponding agent
    if (player == agent1.getPlayer()) {
      viewCmd.ifPresent(view1::accept);
      modelCmd.ifPresent(modelCommand -> {
        try {
          modelCommand.runCommandAs(model, player);
        } catch (IllegalCommandException e) {
          view1.showErrorMessage(e.getMessage());
        }
      });
    } else {
      viewCmd.ifPresent(view2::accept);
      modelCmd.ifPresent(modelCommand -> {
        try {
          modelCommand.runCommandAs(model, player);
        } catch (IllegalCommandException e) {
          view2.showErrorMessage(e.getMessage());
        }
      });
    }
    pollAgents();
  }

  /**
   * Poll the agents according to the type of Controller. For example, a synchronous Controller will
   * continuously poll until the end of the game. An asynchronous Controller will return after
   * polling its agents.
   */
  protected abstract void pollAgents();

  protected final void renderViews() {
    // a bit of a hack, but should return focus if the view requires it
    boolean focus = false;
    // always render at least one view
    if (agent1.viewIsVisible() || !agent2.viewIsVisible()) {
      focus = view1.render();
    }
    if (agent2.viewIsVisible() && view1 != view2) {
      view2.render();
    }
    if (focus) {
      view1.render();
    }
  }

  /**
   * Perform the current pending commands for an Agent.
   *
   * @param agent the agent to perform the commands for
   * @param view  the view to apply the commands to
   * @return true iff a command was performed
   */
  protected final boolean performCommands(AbsoluteAgent agent, ReversiView view) {
    UnifiedCommand cmd = getAgentCommand(agent, view);
    Optional<ViewCommand> viewCmd = cmd.getViewCommand();
    Optional<ModelCommand> modelCmd = cmd.getModelCommand();
    boolean isMyTurn = model.getPlayer() == agent.getPlayer();

    boolean performedCommand = isMyTurn
        && (viewCmd.isPresent()
        || (modelCmd.isPresent() && !modelCmd.get().equals(new Pass())));

    if (modelCmd.isPresent() && isMyTurn) {
      try {
        modelCmd.get().runCommandAs(model, agent.getPlayer());
      } catch (IllegalCommandException e) {
        view.showErrorMessage(e.getMessage());
      }
    }
    viewCmd.ifPresent(viewCommand -> viewCommand.performViewCommand(view, model));
    return performedCommand;
  }

  private UnifiedCommand getAgentCommand(AbsoluteAgent agent, ReversiView view) {
    UnifiedCommand retCmd = null;
    while (retCmd == null) {
      try {
        retCmd = agent.getAndResetCommand();
      } catch (RuntimeException e) {
        System.err.println(e.getMessage());
        view.showErrorMessage(e.getMessage());
      }
    }
    return retCmd;
  }
}
