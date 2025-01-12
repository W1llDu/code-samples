package reversi.controller;

import reversi.controller.agent.AbsoluteAgent;
import reversi.model.MutableReversiModel;
import reversi.view.ReversiView;

/**
 * Asynchronous Controller for Reversi. Typically used in applications involving GUIs instead of
 * TUIs, where the players share different views. Usually the players will have their own views to
 * play the game on, unless it's an AI. AI can optionally choose to visualize their actions using an
 * Agent which decorates it.
 */
public class AsynchronousReversiController extends AbstractReversiController {
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
   * @param model  the model to use to play the game
   * @throws NullPointerException if any argument is null
   */
  public AsynchronousReversiController(AbsoluteAgent agent1,
                                       AbsoluteAgent agent2,
                                       ReversiView view1,
                                       ReversiView view2,
                                       MutableReversiModel model) {
    super(agent1, agent2, view1, view2, model);
    ControllerCallback p1Callback = new ControllerCallbackImpl(this, view1, agent1.getPlayer());
    ControllerCallback p2Callback = new ControllerCallbackImpl(this, view2, agent2.getPlayer());
    view1.setControllerCallback(p1Callback);
    view2.setControllerCallback(p2Callback);
  }

  @Override
  protected void pollAgents() {
    // while there are commands being performed, continue to check. Only when commands are finished
    // should we stop checking. Render the view between each invocation as an extra precaution
    boolean performedCommand = true;
    while (performedCommand) {
      performedCommand = performCommands(agent1, view1);
      performedCommand = performCommands(agent2, view2) || performedCommand;
      renderViews();
    }
    if (isGameOver()) {
      view1.gameEnd();
      if (view1 != view2) {
        view2.gameEnd();
      }
    }
  }
}
